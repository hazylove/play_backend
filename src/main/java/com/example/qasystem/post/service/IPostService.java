package com.example.qasystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Post;
import com.example.qasystem.post.domain.query.PostQuery;

public interface IPostService extends IService<Post> {
    PageList<Post> getPostList(PostQuery postQuery);

    void insert(Post post);

    Post selectById(Long id);
}
