package com.example.qasystem.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Comment;
import com.example.qasystem.post.domain.query.CommentQuery;
import com.example.qasystem.post.mapper.CommentMapper;
import com.example.qasystem.post.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageList<Comment> getMainCommentList(CommentQuery commentQuery) {
        //条数
        Long total = commentMapper.count(commentQuery);
        //分页数据
        List<Comment> comments = commentMapper.getMainCommentList(commentQuery);

        return new PageList<>(total, comments);

    }

    @Override
    @Transactional
    public void insert(Comment comment) {
        comment.setCommentCreatedDate(Calendar.getInstance().getTime());
        commentMapper.insert(comment);
    }

    @Override
    public PageList<Comment> getSubCommentList(CommentQuery commentQuery) {
        //条数
        Long total = commentMapper.count(commentQuery);
        //分页数据
        List<Comment> comments = commentMapper.getSubCommentList(commentQuery);

        return new PageList<>(total, comments);
    }

}
