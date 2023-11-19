package com.example.qasystem.org.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    // 用户名
    private String username;
    // 密码
    private String password;
    // 验证码
    private String verificationCode;
}
