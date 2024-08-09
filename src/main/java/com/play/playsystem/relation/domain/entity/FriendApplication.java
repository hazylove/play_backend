package com.play.playsystem.relation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Long user_id;

    /**
     * 被申请用户id
     */
    private Long apply_user_id;

    /**
     * 已读
     */
    private Boolean beRead;

    /**
     * 申请状态 -1拒绝 1同意 0未处理
     */
    private int status;

    /**
     * 创建时间
     */
    private LocalDateTime created_date;

}
