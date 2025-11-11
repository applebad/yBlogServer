package com.yblog.service.userservice.Service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yblog.service.userservice.Entity.DTO.UserDTO;
import com.yblog.service.userservice.Entity.Mapper.UserMapper;
import com.yblog.service.userservice.Entity.User;
import com.yblog.service.userservice.Service.UserService;
import com.yblog.service.userservice.Utils.RedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private RedisTemplate<String, Object> redisTemplate;
    private RedisService redisService;
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    @Autowired
    public UserServiceImpl(UserMapper userMapper, RedisTemplate<String, Object> redisTemplate, RedisService redisService){
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.redisService = redisService;
    }



    public UserDTO getUserById(String uid){
        UserDTO userDTO = new UserDTO();
        User user = redisService.get(uid, User.class);
        if(user == null){
            user = userMapper.selectByMap(Map.of("uid",uid)).getFirst();
            if(user != null){
                userDTO.setUser(user);
                redisService.set(uid, userDTO, CACHE_TTL);
            }
        }
        return userDTO;
    }

    public IPage<UserDTO> getUsers(int page, int size) {
        IPage<User> userPage = new Page<>(page,size);
        userMapper.selectPage(userPage, null);
        return userPage.convert(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        });
    }
    public IPage<UserDTO> getUsers(int page) {return getUsers(page, 10);}

    public boolean updateUser(UserDTO userDTO) {
        try {
            if(userDTO.getUid()==null) return false;
            User user = userMapper.getUserByUID(userDTO.getUid());
            if (user != null) {
                BeanUtils.copyProperties(userDTO, user);
                return userMapper.updateById(user) > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(String uid) {
        try{
            return userMapper.deleteByMap(Map.of("uid",uid)) > 0;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean addUser(UserDTO userDTO) {
        try{
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            return userMapper.insert(user) > 0;
        }catch (Exception e){
            return false;
        }
    }
}
