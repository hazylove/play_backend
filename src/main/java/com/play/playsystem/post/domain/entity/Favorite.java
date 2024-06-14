package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_favorite")
public class Favorite {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收藏夹名称
     */
    private String favoriteName;

    /**
     * 所属用户id
     */
    private Long createdId;

    /**
     * 描述
     */
    private String introduction;

    /**
     * 帖子数量
     */
    private int postNum;

    /**
     * 是否公开
     */
    private boolean opened;

    /**
     * 最新更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
}
