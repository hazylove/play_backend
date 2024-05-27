package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;

public interface IPostService extends IService<Post> {
    PageList<Post> getPostList(PostQuery postQuery);

    void insert(Post post);

    Post selectById(Long id);
}
