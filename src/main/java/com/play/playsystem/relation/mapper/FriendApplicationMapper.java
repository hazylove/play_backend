package com.play.playsystem.relation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.relation.domain.entity.FriendApplication;
import com.play.playsystem.relation.domain.query.RelationQuery;
import com.play.playsystem.relation.domain.vo.FriendApplicationVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendApplicationMapper extends BaseMapper<FriendApplication> {
    Long countFriendApplication(RelationQuery relationQuery);

    List<FriendApplicationVo> getFriendApplicationList(RelationQuery relationQuery);
}
