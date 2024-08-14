package com.play.playsystem.relation.domain.vo;

import com.play.playsystem.basic.constant.FriendRequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplicationVo {
    /**
     * id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 申请消息
     */
    private String applyInfo;

    /**
     * 申请状态
     */
    private FriendRequestStatusEnum status;
}
