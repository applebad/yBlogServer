package com.yblog.gate.postservice.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yblog.gate.postservice.Entity.Post;

public interface PostService {

    public IPage<Post> getPosts(int page, int size);
    public IPage<Post> getPosts(int page);
    public Post getPost(String uid);
}
