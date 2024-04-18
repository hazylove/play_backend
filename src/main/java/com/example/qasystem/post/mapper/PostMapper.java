package com.example.qasystem.post.mapper;


import com.example.qasystem.post.domain.entity.Post;
import com.example.qasystem.post.domain.query.PostQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    Long count(PostQuery postQuery);

    List<Post> getPostList(PostQuery postQuery);

    void insert(Post post);

    Post selectById(Long id);
}
