package com.yblog.gate.testservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {

    @GetMapping("/hello")
    public String test() {return "hello";}

    @GetMapping("/get/{id}")
    public String get(@PathVariable int id) {
        return "hello" + id;
    }
}
