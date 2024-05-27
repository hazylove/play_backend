package com.play.playsystem.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // id
    private String username; // 账号
    private String password; // 密码
    private String nickname; // 昵称
    private String avatar; // 头像
    private String email; // 邮箱
    private String phone; // 电话
    private Date createdDate; // 创建时间
}
