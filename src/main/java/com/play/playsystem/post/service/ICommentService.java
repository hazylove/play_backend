package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.query.CommentQuery;

public interface ICommentService extends IService<Comment> {
    PageList<Comment> getMainCommentList(CommentQuery commentQuery);

    void insert(Comment comment);

    PageList<Comment> getSubCommentList(CommentQuery commentQuery);
}
