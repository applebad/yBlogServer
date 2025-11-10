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

    private String password_hash;

    private String display_name;

    private String avatar_url;
    //个人简介？
    private String bio;
    
    private Role role;
    
    private Status status;
    
    private Timestamp last_login_at;
    
    private Timestamp created_at;

    private Timestamp updated_at;

    private Timestamp deleted_at;

    public UserDTO(){}

    public UserDTO(User user){
        setUser(user);
    }
    public boolean setUser(User user){
        try {
            this.uid = user.getUid();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.password_hash = user.getPassword_hash();
            this.display_name = user.getDisplay_name();
            this.avatar_url = user.getAvatar_url();
            this.bio = user.getBio();
            this.role = user.getRole();
            this.status = user.getStatus();
            this.last_login_at = user.getLast_login_at();
            this.created_at = user.getCreated_at();
            this.updated_at = user.getUpdated_at();
            this.deleted_at = user.getDeleted_at();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
