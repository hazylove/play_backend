package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.mapper.PostMapper;
import com.play.playsystem.post.service.IPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Resource
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
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        //String utcTime = sdf.format(post.getPostCreatedDate());
        //System.out.println(utcTime);
        //postMapper.insert(post);

        post.setPostCreatedDate(Calendar.getInstance().getTime());
        postMapper.insert(post);
    }

    @Override
    public Post selectById(Long id) {
        return postMapper.selectById(id);
    }
}




