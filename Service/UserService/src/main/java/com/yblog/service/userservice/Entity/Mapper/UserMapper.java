package com.yblog.service.userservice.Entity.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yblog.service.userservice.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from users")
    List<User> getUsers();

    @Select("SELECT * FROM users WHERE uid = #{uid}")
    User getUserByUID(@Param("uid") String UID);
}
