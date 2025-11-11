package com.yblog.gate.postservice.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("posts")
public class Post {
    private int id;
    private String uid;
    private String title;
    private String summary;
    private String content;

    @TableField("cover_image")
    private String cover_image;

    @TableField("author_uid")
    private String author_uid;

    @TableField("category_uid")
    private String category_uid;

    @TableField("status")
    private Status status;

    @TableField("comment_status")
    private CommentStatus comment_status;

    @TableField("view_count")
    private Long view_count;

    @TableField("comment_count")
    private Long comment_count;

    @TableField("like_count")
    private Long like_count;

    @TableField("is_top")
    private boolean is_top;

    @TableField("is_recommended")
    private boolean is_recommended;

    @TableField("published_at")
    private Timestamp publish_at;

    @TableField("created_at")
    private Timestamp created_at;

    @TableField("updated_at")
    private Timestamp updated_at;

    @TableField("deleted_at")
    private Timestamp deleted_at;
}
