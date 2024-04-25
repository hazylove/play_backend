package com.example.qasystem.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.qasystem.post.domain.entity.Comment;
import com.example.qasystem.post.domain.query.CommentQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    Long count(CommentQuery commentQuery);

    List<Comment> getMainCommentList(CommentQuery commentQuery);

    List<Comment> getSubCommentList(CommentQuery commentQuery);
}
