package com.play.playsystem.relation.service;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.relation.domain.query.FollowQuery;

public interface IRelationService {

    /**
     * 关注取消关注
     * @param followedUserId 被关注用户id
     * @param userId 当前用户id
     */
    JsonResult follow(Long followedUserId, Long userId);

    /**
     * 关注列表
     * @param followQuery 查询参数
     */
    JsonResult getFollowList(FollowQuery followQuery);
}
