package com.yblog.service.userservice.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

;

@Data
@TableName(value = "users")
public class User {
    @TableField("id")
    private Long id;

    @TableField("uid")
    private String uid;

    @TableField("username")
    private String username;
    @TableField("email")
    private String email;

    @TableField("password_hash")
    private String password_hash;

    @TableField("display_name")
    private String display_name;

    @TableField("avatar_url")
    private String avatar_url;

    @TableField("bio")
    private String bio;

    @TableField("role")
    private Role role;

    @TableField("status")
    private Status status;

    @TableField("last_login_at")
    private Timestamp last_login_at;

    @TableField("created_at")
    private Timestamp created_at;

    @TableField("updated_at")
    private Timestamp updated_at;

    @TableField("deleted_at")
    private Timestamp deleted_at;

}
