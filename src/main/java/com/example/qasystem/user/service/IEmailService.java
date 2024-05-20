package com.example.qasystem.user.service;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.user.domain.dto.EmailDto;

public interface IEmailService {

    /**
     * 发送邮件
     * @param emailDto 邮件dto
     */
    void sendEmail(EmailDto emailDto);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return JsonResult
     */
    JsonResult sendRegisterCode(String email);

    JsonResult sendChangeCode(Long userId, String email);
}
