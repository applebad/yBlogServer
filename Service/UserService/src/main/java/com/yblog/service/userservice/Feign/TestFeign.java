package com.yblog.service.userservice.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "test-service", path = "/test")
public interface TestFeign {

    @GetMapping("/hello")
    public String hello();
    @GetMapping("/get/{id}")
    public String getTest(@PathVariable int id);

}
