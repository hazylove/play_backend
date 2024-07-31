package com.play.playsystem.relation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.relation.domain.entity.UserUserFollow;
import com.play.playsystem.relation.domain.query.FollowQuery;
import com.play.playsystem.user.domain.vo.UserListVo;

import java.util.List;

public interface UserUserFollowMapper extends BaseMapper<UserUserFollow> {

    List<UserListVo> getFollowList(FollowQuery followQuery);

    Long countFollow(FollowQuery followQuery);

    Long countFans(FollowQuery followQuery);

    List<UserListVo> getFansList(FollowQuery followQuery);
}
