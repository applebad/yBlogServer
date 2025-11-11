package com.yblog.gate.postservice.Config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yblog.gate.postservice.Entity.Mapper")
public class MybatisConfig {
}
