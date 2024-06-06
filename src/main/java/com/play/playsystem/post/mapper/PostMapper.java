package com.play.playsystem.post.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    Long count(PostQuery postQuery);

    List<PostVo> getPostList(PostQuery postQuery);

    PostVo selectById(Long id, Long userId);
}
