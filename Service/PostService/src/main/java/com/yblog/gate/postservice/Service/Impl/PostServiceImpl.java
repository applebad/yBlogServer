package com.yblog.gate.postservice.Service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yblog.gate.postservice.Entity.Mapper.PostMapper;
import com.yblog.gate.postservice.Entity.Post;
import com.yblog.gate.postservice.Service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.util.Map;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private PostMapper postMapper;
    private RedisTemplate<String, Object> redisTemplate;
    private static final java.time.Duration CACHE_TTL = java.time.Duration.ofHours(1);

    @Autowired
    public PostServiceImpl(PostMapper postMapper,RedisTemplate<String, Object> redisTemplate){
        this.postMapper = postMapper;
        this.redisTemplate = redisTemplate;
    }

    public IPage<Post> getPosts(int page, int size){
        IPage<Post> postPage = new Page<>(page, size);
        postPage = postMapper.selectPage(postPage,null);
        return postPage.convert(post -> {
            post.setId(0);
            return post;
        });
    }
    public IPage<Post> getPosts(int page){return getPosts(page, 10);}

    public Post getPost(String uid){
        Post post = null;
        Object result = null;
        if(isRedisAvailable()){
            result = redisTemplate.opsForValue().get(uid);
            if(result != null){
                post = (Post) result;
                redisTemplate.opsForValue().set(uid, post, CACHE_TTL);
                return post;
            }else{
                post = postMapper.selectByMap(Map.of("uid",uid)).getFirst();
                redisTemplate.opsForValue().set(uid, post, CACHE_TTL);
                return post;
            }
        }else{
            post = postMapper.selectByMap(Map.of("uid",uid)).getFirst();
        }
        return post;
    }

    private boolean isRedisAvailable() {
        try {
            // 简单的ping操作检查连接
            redisTemplate.getConnectionFactory().getConnection().ping();
            return true;
        } catch (Exception e) {
            log.warn("Redis连接检查失败: {}", e.getMessage());
            return false;
        }
    }
}
