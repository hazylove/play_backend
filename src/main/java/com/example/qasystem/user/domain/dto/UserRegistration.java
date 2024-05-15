package com.example.qasystem.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
    private Long id;
    private String username;
    private String password1;
    private String password2;
    private String phone;
    private String email;
    private Date createdDate;

    // 邮箱验证码
    private String emailCode;
}
