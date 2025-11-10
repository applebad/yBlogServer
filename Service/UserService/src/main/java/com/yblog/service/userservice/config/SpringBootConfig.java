package com.yblog.service.userservice.config;

import com.yblog.service.userservice.ResultMap.yBlogResultMap;
import com.yblog.service.userservice.Utils.yBlogCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringBootConfig {
    @Bean
    @Scope("prototype")
    public yBlogResultMap yBlogResultMap() {
        return new yBlogResultMap();
    }
    @Bean
    public yBlogCode yBlogCode() { return new yBlogCode();}
}
