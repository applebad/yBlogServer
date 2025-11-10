package com.yblog.service.userservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@Slf4j
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key 序列化 - 固定使用 String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value 序列化 - 使用优化的序列化器
        template.setValueSerializer(optimizedJsonRedisSerializer());
        template.setHashValueSerializer(optimizedJsonRedisSerializer());

        // 启用事务支持
        template.setEnableTransactionSupport(true);

        // 初始化
        template.afterPropertiesSet();

        log.info("优化版 RedisTemplate 配置完成");
        return template;
    }

    @Bean
    public RedisSerializer<Object> optimizedJsonRedisSerializer() {
        return new OptimizedJacksonRedisSerializer();
    }

    /**
     * 优化的 Jackson 序列化器
     * 核心改进：添加序列化缓存、预编译类型信息
     */
    public static class OptimizedJacksonRedisSerializer implements RedisSerializer<Object> {

        private final ObjectMapper objectMapper;

        // 序列化结果缓存 - 减少重复序列化开销
        private final ConcurrentHashMap<String, byte[]> serializationCache;

        // 类型信息缓存 - 加速反序列化
        private final ConcurrentHashMap<String, JavaType> typeCache;

        // 统计信息
        private final AtomicLong cacheHitCount = new AtomicLong();
        private final AtomicLong totalRequestCount = new AtomicLong();

        public OptimizedJacksonRedisSerializer() {
            this.objectMapper = createOptimizedObjectMapper();
            this.serializationCache = new ConcurrentHashMap<>(1024);
            this.typeCache = new ConcurrentHashMap<>(256);

            // 定期清理缓存，防止内存泄漏
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(this::cleanupCache, 1, 1, TimeUnit.HOURS);
        }

        private ObjectMapper createOptimizedObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();

            // 1. 禁用不必要的特性，提升性能
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(MapperFeature.USE_ANNOTATIONS, true);

            // 2. 注册必要的模块
//            mapper.registerModule(new Hibernate5Module());
            mapper.registerModule(new JavaTimeModule());

            // 3. 配置序列化 inclusions
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // 4. 启用一些性能优化特性
            mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);

            return mapper;
        }

        @Override
        public byte[] serialize(Object object) throws SerializationException {
            if (object == null) {
                return new byte[0];
            }

            totalRequestCount.incrementAndGet();

            try {
                // 生成缓存键：类名 + 内容哈希（避免相同对象重复序列化）
                String cacheKey = generateCacheKey(object);

                // 检查缓存
                byte[] cached = serializationCache.get(cacheKey);
                if (cached != null) {
                    cacheHitCount.incrementAndGet();
                    log.debug("序列化缓存命中: {}", cacheKey);
                    return cached;
                }

                // 缓存未命中，执行序列化
                long startTime = System.nanoTime();
                byte[] result = objectMapper.writeValueAsBytes(object);
                long cost = System.nanoTime() - startTime;

                // 只缓存较小的对象，避免内存占用过大
                if (result.length < 1024 * 10) { // 10KB 以下
                    serializationCache.put(cacheKey, result);
                }

                if (cost > 1000000) { // 超过 1ms 记录警告
                    log.warn("序列化性能警告: {} 耗时 {}ns, 大小 {}bytes",
                            object.getClass().getSimpleName(), cost, result.length);
                }

                return result;

            } catch (Exception e) {
                throw new SerializationException("序列化失败: " + object.getClass(), e);
            }
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }

            totalRequestCount.incrementAndGet();

            try {
                long startTime = System.nanoTime();

                // 直接反序列化为 Object，避免类型猜测的开销
                Object result = objectMapper.readValue(bytes, Object.class);

                long cost = System.nanoTime() - startTime;

                if (cost > 1000000) { // 超过 1ms 记录警告
                    log.warn("反序列化性能警告: 耗时 {}ns, 数据大小 {}bytes", cost, bytes.length);
                }

                return result;

            } catch (Exception e) {
                throw new SerializationException("反序列化失败", e);
            }
        }

        /**
         * 生成缓存键：类名 + 内容哈希
         */
        private String generateCacheKey(Object object) {
            try {
                String className = object.getClass().getName();
                String contentHash = Integer.toHexString(object.hashCode());

                // 对于集合类型，添加元素数量信息
                if (object instanceof Collection) {
                    int size = ((Collection<?>) object).size();
                    return className + ":" + size + ":" + contentHash;
                } else if (object instanceof Map) {
                    int size = ((Map<?, ?>) object).size();
                    return className + ":" + size + ":" + contentHash;
                }

                return className + ":" + contentHash;

            } catch (Exception e) {
                // 如果哈希计算失败，使用随机数
                return object.getClass().getName() + ":" + System.currentTimeMillis();
            }
        }

        /**
         * 定期清理缓存
         */
        private void cleanupCache() {
            int beforeSize = serializationCache.size();

            // 简单的 LRU 策略：清理一半的缓存
            if (serializationCache.size() > 1024) {
                int targetSize = serializationCache.size() / 2;
                Iterator<String> iterator = serializationCache.keySet().iterator();

                int removed = 0;
                while (iterator.hasNext() && serializationCache.size() > targetSize) {
                    iterator.next();
                    iterator.remove();
                    removed++;
                }

                log.info("序列化缓存清理: {} -> {}, 移除 {} 个条目",
                        beforeSize, serializationCache.size(), removed);
            }

            // 输出缓存命中率统计
            long total = totalRequestCount.get();
            long hit = cacheHitCount.get();
            if (total > 0) {
                double hitRate = (double) hit / total * 100;
                log.info("序列化缓存统计: 总数 {}, 命中 {}, 命中率 {:.2f}%",
                        total, hit, hitRate);
            }
        }

        /**
         * 获取性能统计信息
         */
        public Map<String, Object> getStats() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("cacheSize", serializationCache.size());
            stats.put("totalRequests", totalRequestCount.get());
            stats.put("cacheHits", cacheHitCount.get());
            stats.put("typeCacheSize", typeCache.size());

            long total = totalRequestCount.get();
            long hit = cacheHitCount.get();
            if (total > 0) {
                stats.put("hitRate", (double) hit / total * 100);
            }

            return stats;
        }
    }
}
