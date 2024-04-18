package com.example.qasystem.post.service.impl;

import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Post;
import com.example.qasystem.post.domain.query.PostQuery;
import com.example.qasystem.post.mapper.PostMapper;
import com.example.qasystem.post.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class PostServiceImpl implements IPostService {
    @Autowired
    private PostMapper postMapper;
    @Override
    public PageList<Post> getPostList(PostQuery postQuery) {
        //条数
        Long total = postMapper.count(postQuery);
        //分页数据
        List<Post> posts = postMapper.getPostList(postQuery);
        return new PageList<>(total, posts);
    }

    @Override
    @Transactional
    public void insert(Post post) {
        post.setPostCreatedDate(Calendar.getInstance().getTime());
        postMapper.insert(post);
    }

    @Override
    public Post selectById(Long id) {
        return postMapper.selectById(id);
    }
}




