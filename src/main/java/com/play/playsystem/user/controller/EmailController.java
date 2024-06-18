package com.play.playsystem.user.controller;


import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.user.service.IEmailService;
import com.play.playsystem.user.utils.UserCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/email")
@Slf4j
public class EmailController {

    @Autowired
    private IEmailService emailService;

    /**
     * 发送注册邮箱验证码
     * @param email 邮箱
     */
    @PostMapping("/sendRegisterCode")
    public JsonResult sendRegisterCode(@RequestParam String email) {
        return emailService.sendRegisterCode(email);
    }

    /**
     * 发送修改密码邮箱验证码
     * @param email 邮箱
     */
    @PostMapping("/sendChangeCode")
    public JsonResult sendChangeCode(@RequestParam String email) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return emailService.sendChangeCode(userId, email);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 发送登录邮箱验证码
     * @param email 邮箱
     */
    @PostMapping("/sendLoginCode")
    public JsonResult sendLoginCode(@RequestParam String email) {
        return emailService.sendLoginCode(email);
    }
}
