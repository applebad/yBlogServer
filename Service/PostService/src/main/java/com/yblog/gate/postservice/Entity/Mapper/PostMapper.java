package com.yblog.gate.postservice.Entity.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yblog.gate.postservice.Entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
