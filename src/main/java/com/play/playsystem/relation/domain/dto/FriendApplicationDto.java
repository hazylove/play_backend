package com.play.playsystem.relation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplicationDto {
    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 申请用户id
     */
    private Long applyUserId;

    /**
     * 申请信息
     */
    private String applyInfo;
}
