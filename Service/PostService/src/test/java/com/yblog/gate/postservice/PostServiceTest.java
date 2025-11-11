package com.yblog.gate.postservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yblog.gate.postservice.Entity.Post;
import com.yblog.gate.postservice.Service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {
    private PostService postService;

    @Autowired
    public PostServiceTest(PostService postService){
        this.postService = postService;
    }

    @Test
    public void getPagePosts(){
        IPage<Post> page = postService.getPosts(1,2);
        System.out.println(page.getRecords());
        System.out.println(page.getTotal());
        assert page.getRecords().size() > 0;
    }
    @Test
    public void getPostByUId(){
        Post post = postService.getPost("POST001");
        System.out.println(post.toString());
        assert post.getUid().equals("POST001");
    }
}
