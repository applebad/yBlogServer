package com.yblog.service.userservice.controller;

import com.yblog.service.userservice.Entity.DTO.UserDTO;
import com.yblog.service.userservice.Feign.TestFeign;
import com.yblog.service.userservice.ResultMap.yBlogResultMap;
import com.yblog.service.userservice.Service.UserService;
import com.yblog.service.userservice.Utils.yBlogCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final WebApplicationContext webApplicationContext;
    private TestFeign testFeign;
    private UserService userService;
    @Autowired
    public UserController(TestFeign testFeign,
                          UserService userService,
                          WebApplicationContext webApplicationContext
                          ){
        this.testFeign = testFeign;
        this.userService = userService;
        this.webApplicationContext = webApplicationContext;
    }

    @GetMapping("/getAll")
    public Map<String,Object> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        yBlogResultMap resultMap = webApplicationContext.getBean(yBlogResultMap.class);
        resultMap.addMap(yBlogCode.DATA,userService.getUsers(page,size));
        resultMap.addMap(yBlogCode.STATUS,true);
        return resultMap.getResultMap();

    }

    @GetMapping("/{id}")
    public Map<String,Object> getUser(@PathVariable String id) {
        yBlogResultMap resultMap = webApplicationContext.getBean(yBlogResultMap.class);
        UserDTO userDTO = userService.getUserById(id);
        if(userDTO == null) return resultMap.getResultMap();
        resultMap.addMap(yBlogCode.DATA,userDTO);
        resultMap.addMap("status",true);
        return resultMap.getResultMap();
    }

    @PostMapping
    public Map<String,Object> addUser(@RequestBody UserDTO userDTO) {
        yBlogResultMap resultMap = webApplicationContext.getBean(yBlogResultMap.class);
        if(userService.updateUser(userDTO)){
            resultMap.addMap(yBlogCode.STATUS,true);
        }
        return resultMap.getResultMap();
    }
}
