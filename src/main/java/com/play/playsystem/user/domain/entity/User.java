package com.play.playsystem.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar; // 头像

    /**
     * 邮箱
     */
    private String email; // 邮箱

    /**
     * 电话
     */
    private String phone; // 电话

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate; // 创建时间

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 简介
     */
    private String profile;

    /**
     * 出生日期
     */
    private LocalDate birth;
}
