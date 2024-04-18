package com.example.qasystem.post.mapper;

import com.example.qasystem.post.domain.entity.Comment;
import com.example.qasystem.post.domain.query.CommentQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    Long count(CommentQuery commentQuery);

    List<Comment> getMainCommentList(CommentQuery commentQuery);

    void insert(Comment comment);

    List<Comment> getSubCommentList(CommentQuery commentQuery);
}
