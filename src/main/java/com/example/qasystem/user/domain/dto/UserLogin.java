package com.example.qasystem.user.domain.dto;

import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserLogin extends UserBase {
    // 密码
    private String password;
    // 验证码
    private String verificationCode;
}
