package com.play.playsystem.post.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    Long count(PostQuery postQuery);

    List<PostVo> getPostList(PostQuery postQuery);

    PostVo selectById(Long id, Long userId);

    Long countLikePost(PostQuery postQuery);

    List<PostVo> getLikePostList(PostQuery postQuery);

    @Select("SELECT tp.post_created_id " +
            "FROM forum.t_post tp " +
            "LEFT JOIN forum.t_comment tc on tc.comment_post_id = tp.id " +
            "WHERE tc.id = #{commentId}")
    Long getCratedIdByCommentId(Long commentId);

    @Select("SELECT post_created_id " +
            "FROM t_post " +
            "WHERE id = #{postId}")
    Long getCreatedIdByPostId(Long postId);
}
