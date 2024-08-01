package com.play.playsystem.relation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.relation.domain.entity.UserUserBlock;
import com.play.playsystem.relation.domain.query.RelationQuery;
import com.play.playsystem.user.domain.vo.UserListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserUserBlockMapper extends BaseMapper<UserUserBlock> {
    Long countBlock(RelationQuery relationQuery);

    List<UserListVo> getBlockList(RelationQuery relationQuery);
}
