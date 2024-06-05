package com.play.playsystem.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.query.CommentQuery;
import com.play.playsystem.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    Long count(CommentQuery commentQuery);

    List<Comment> getMainCommentList(CommentQuery commentQuery);

    List<Comment> getSubCommentList(CommentQuery commentQuery);

    @Select("SELECT tu.id, tu.nickname " +
            "FROM t_user tu " +
            "LEFT JOIN forum.t_comment tc on tu.id = tc.comment_created_id " +
            "WHERE tc.id = #{commentId}")
    User getCommentCreatedBy(@Param("commentId") Long commentId);
}
