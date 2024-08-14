package com.play.playsystem.relation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.play.playsystem.basic.constant.FriendRequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend_application")
public class FriendApplication {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 被申请用户id
     */
    private Long applyUserId;

    /**
     * 申请信息
     */
    private String applyInfo;

    /**
     * 已读
     */
    private Boolean beRead;

    /**
     * 申请状态
     */
    private FriendRequestStatusEnum status;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
}
