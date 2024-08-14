package com.play.playsystem.relation.service;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.relation.domain.dto.FriendApplicationDto;
import com.play.playsystem.relation.domain.query.RelationQuery;

import java.io.IOException;

public interface IRelationService {

    /**
     * 关注取消关注
     * @param followedUserId 被关注用户id
     * @param userId 当前用户id
     */
    JsonResult follow(Long followedUserId, Long userId);

    /**
     * 关注列表
     * @param relationQuery 查询参数
     */
    JsonResult getFollowList(RelationQuery relationQuery);

    /**
     * 粉丝列表
     * @param relationQuery 查询参数
     */
    JsonResult getFansList(RelationQuery relationQuery);

    /**
     * 拉黑用户
     * @param blockedUserId 拉黑用户id
     * @param userId 当前用户id
     */
    JsonResult block(Long blockedUserId, Long userId);

    /**
     * 查看拉黑列表
     * @param relationQuery 查询参数
     */
    JsonResult getBlockList(RelationQuery relationQuery);

    /**
     * 添加好友
     * @param friendApplicationDto 申请信息
     */
    JsonResult addFriend(FriendApplicationDto friendApplicationDto) throws IOException;

    /**
     * 获取好友申请列表
     * @param relationQuery 查询参数
     */
    JsonResult getFriendApplicationList(RelationQuery relationQuery);
}
