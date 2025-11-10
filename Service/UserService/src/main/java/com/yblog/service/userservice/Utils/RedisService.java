package com.yblog.service.userservice.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 安全获取对象 - 自动处理类型转换
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return null;
            }

            // 如果已经是目标类型，直接返回
            if (clazz.isInstance(value)) {
                return (T) value;
            }

            // 如果是 LinkedHashMap（JSON 反序列化的默认结果），转换为目标类型
            if (value instanceof Map) {
                return convertMapToObject((Map<String, Object>) value, clazz);
            }

            // 其他情况尝试强制转换
            return (T) value;

        } catch (Exception e) {
            log.error("Redis 获取数据失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 将 Map 转换为目标对象
     */
    private <T> T convertMapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            return objectMapper.convertValue(map, clazz);
        } catch (Exception e) {
            log.error("Map 转换对象失败, target: {}", clazz.getSimpleName(), e);
            return null;
        }
    }

    /**
     * 存储对象
     */
    public <T> void set(String key, T value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            log.error("Redis 存储数据失败, key: {}", key, e);
        }
    }

    public <T> void set(String key, T value) {
        set(key, value, null);
    }

    /**
     * 删除键
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis 删除数据失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 检查键是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis 检查键失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, Duration timeout) {
        try {
            return redisTemplate.expire(key, timeout);
        } catch (Exception e) {
            log.error("Redis 设置过期时间失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 批量获取
     */
    public <T> List<T> multiGet(List<String> keys, Class<T> clazz) {
        try {
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);
            if (values == null) {
                return new ArrayList<>();
            }

            return values.stream()
                    .filter(Objects::nonNull)
                    .map(value -> convertObject(value, clazz))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Redis 批量获取失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 通用对象转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertObject(Object value, Class<T> clazz) {
        if (clazz.isInstance(value)) {
            return (T) value;
        }

        if (value instanceof Map) {
            return convertMapToObject((Map<String, Object>) value, clazz);
        }

        try {
            return (T) value;
        } catch (ClassCastException e) {
            log.warn("类型转换失败: {} -> {}", value.getClass(), clazz);
            return null;
        }
    }
}
