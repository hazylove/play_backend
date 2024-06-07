package com.play.playsystem.user.service;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.user.domain.dto.EmailDto;

public interface IEmailService {

    /**
     * 发送邮件
     * @param emailDto 邮件dto
     */
    void sendEmail(EmailDto emailDto);

    /**
     * 发送注册 邮箱验证码
     * @param email 邮箱
     */
    JsonResult sendRegisterCode(String email);

    /**
     * 发送修改密码 邮箱验证吗
     * @param userId 用户id
     * @param email 邮箱
     */
    JsonResult sendChangeCode(Long userId, String email);

    /**
     * 发送登录 邮箱验证码
     * @param email 邮箱
     */
    JsonResult sendLoginCode(String email);
}
