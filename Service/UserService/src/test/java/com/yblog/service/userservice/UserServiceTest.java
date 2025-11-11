package com.yblog.service.userservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yblog.service.userservice.Entity.DTO.UserDTO;
import com.yblog.service.userservice.Entity.Mapper.UserMapper;
import com.yblog.service.userservice.Entity.Role;
import com.yblog.service.userservice.Entity.Status;
import com.yblog.service.userservice.Feign.TestFeign;
import com.yblog.service.userservice.Service.UserService;
import com.yblog.service.userservice.Utils.yBlogCode;
import com.yblog.service.userservice.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Map;

@SpringBootTest
public class UserServiceTest {

    private UserMapper userMapper;
    private UserService userService;
    private UserController userController;
    private com.yblog.service.userservice.ResultMap.yBlogResultMap yBlogResultMap;
    private WebApplicationContext webApplicationContext;
    private UserDTO userDTO;
    private TestFeign testFeign;

    @Autowired
    public UserServiceTest(UserDTO userDTO,
                                       WebApplicationContext webApplicationContext,
                                       com.yblog.service.userservice.ResultMap.yBlogResultMap yBlogResultMap,
                                       UserController userController,
                                       UserService userService,
                                       UserMapper userMapper,
                                       TestFeign testFeign){
        this.userDTO = userDTO;
        this.webApplicationContext = webApplicationContext;
        this.yBlogResultMap = yBlogResultMap;
        this.userController = userController;
        this.userService = userService;
        this.userMapper = userMapper;
        this.testFeign = testFeign;
    }

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
        UserDTO userDTO = createUser();
        userDTO.setBio("updateUserTest");
        assert userService.updateUser(userDTO);
    }

    @Test
    void addUser(){
        UserDTO userDTO = createUser();
        userDTO.setBio("addUserTest");
        userService.addUser(userDTO);
    }

    @Test
    void testFeignConnection(){
        String ret = "";
        ret = testFeign.hello();
        ret += "|" + testFeign.getTest(2);
        System.out.println(ret);
        assert ret != null;
    }

    @Test
    void deleteUser(){
        String uid = "USR006";
        boolean ret = userService.deleteUser(uid);
        assert ret;
    }


    private UserDTO createUser(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUid("USR006");
        userDTO.setPasswordHash("ddd");
        userDTO.setUsername("还我神ID");
        userDTO.setEmail("ye740923@qq.com");

        userDTO.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setRole(Role.ADMIN);
        userDTO.setBio("我是五字神人");
        return userDTO;
    }
}
