package com.play.playsystem.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    private Long id;
    private String newPassword1;
    private String newPassword2;

    // 邮箱验证码
    private String emailCode;
}
