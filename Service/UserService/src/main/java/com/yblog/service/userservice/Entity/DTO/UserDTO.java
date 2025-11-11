package com.yblog.service.userservice.Entity.DTO;

import com.yblog.service.userservice.Entity.Role;
import com.yblog.service.userservice.Entity.Status;
import com.yblog.service.userservice.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Getter
@Setter
@Component
public class UserDTO {
    //排除数据库id

    private String uid;

    private String username;
    
    private String email;

    private String passwordHash;

    private String displayName;

    private String avatarUrl;
    //个人简介？
    private String bio;
    
    private Role role;
    
    private Status status;
    
    private Timestamp lastLoginAt;
    
    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public UserDTO(){}

    public UserDTO(User user){
        setUser(user);
    }
    public boolean setUser(User user){
        try {
            this.uid = user.getUid();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.passwordHash = user.getPassword_hash();
            this.displayName = user.getDisplay_name();
            this.avatarUrl = user.getAvatar_url();
            this.bio = user.getBio();
            this.role = user.getRole();
            this.status = user.getStatus();
            this.lastLoginAt = user.getLastLogin_at();
            this.createdAt = user.getCreated_at();
            this.updatedAt = user.getUpdated_at();
            this.deletedAt = user.getDeleted_at();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
