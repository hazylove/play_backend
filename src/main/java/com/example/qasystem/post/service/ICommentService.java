package com.example.qasystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Comment;
import com.example.qasystem.post.domain.query.CommentQuery;

public interface ICommentService extends IService<Comment> {
    PageList<Comment> getMainCommentList(CommentQuery commentQuery);

    void insert(Comment comment);

    PageList<Comment> getSubCommentList(CommentQuery commentQuery);
}
