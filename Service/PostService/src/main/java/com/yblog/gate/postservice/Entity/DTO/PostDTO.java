package com.yblog.gate.postservice.Entity.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yblog.gate.postservice.Entity.CommentStatus;
import com.yblog.gate.postservice.Entity.Status;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PostDTO {
    private String uid;

    private String title;

    private String summary;

    private String content;

    private String cover_image;
    
    private String author_uid;
    
    private String category_uid;

    private Status status;

    private CommentStatus comment_status;
    
    private Long view_count;

    private Long comment_count;
    
    private Long like_count;

    private boolean is_top;

    private boolean is_recommended;
    
    private Timestamp publish_at;
    
    private Timestamp created_at;

    private Timestamp updated_at;
    
    private Timestamp deleted_at;
}
