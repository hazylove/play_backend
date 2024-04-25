package com.example.qasystem.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // id
    private String username; // 账号
    private String password; // 密码
    private String email; // 邮箱
    private String phone; // 电话
}
