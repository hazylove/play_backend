package com.play.playsystem.relation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.relation.domain.entity.UserUserFollow;
import com.play.playsystem.relation.domain.query.RelationQuery;
import com.play.playsystem.user.domain.vo.UserListVo;

import java.util.List;

public interface UserUserFollowMapper extends BaseMapper<UserUserFollow> {

    List<UserListVo> getFollowList(RelationQuery relationQuery);

    Long countFollow(RelationQuery relationQuery);

    Long countFans(RelationQuery relationQuery);

    List<UserListVo> getFansList(RelationQuery relationQuery);
}
