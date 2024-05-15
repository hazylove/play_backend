package com.example.qasystem.user.service;

import com.example.qasystem.user.domain.dto.EmailDto;

public interface IEmailService {

    /**
     * 发送邮件
     * @param emailDto 邮件dto
     */
    void sendEmail(EmailDto emailDto);

    int sendEmailCode(String email);
}
