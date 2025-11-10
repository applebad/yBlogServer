package com.yblog.service.userservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yblog.service.userservice.Entity.DTO.UserDTO;
import com.yblog.service.userservice.Entity.Mapper.UserMapper;
import com.yblog.service.userservice.Entity.Role;
import com.yblog.service.userservice.Entity.Status;
import com.yblog.service.userservice.Service.UserService;
import com.yblog.service.userservice.Utils.yBlogCode;
import com.yblog.service.userservice.controller.UserController;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;


@SpringBootTest
class UserServiceApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserController userController;
    @Autowired
    private com.yblog.service.userservice.ResultMap.yBlogResultMap yBlogResultMap;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserDTO userDTO;

    @Test
    void contextLoads() {
    }

    @Test
    void getUsers() {
        IPage<UserDTO> page =  userService.getUsers(1,2);
        System.out.println(page.getRecords());
        assert !page.getRecords().isEmpty();
    }

    @Test
    void getUser(){
        String uid = "USR001";
        Map<String,Object> result = userController.getUser(uid);
        System.out.println(result.get("data"));
        assert (boolean) result.get(yBlogCode.STATUS);
    }

    @Test
    void updateUser(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUid("USR006");
        userDTO.setPassword_hash("ddd");
        userDTO.setUsername("还我神ID");
        userDTO.setEmail("ye740923@qq.com");

        userDTO.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setRole(Role.ADMIN);
        userDTO.setBio("我是五字神人");
        assert userService.updateUser(userDTO);
    }
}
