package com.yblog.gate.testservice.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "test-service", path = "/test") // 统一前缀
public interface TestFeign {

    @GetMapping("/hello")
    public String hello();
    @GetMapping("/get/{id}")
    public String getTest(@PathVariable int id);
}
