package com.example.qasystem.user.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.example.qasystem.basic.utils.RedisUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.user.domain.dto.EmailDto;
import com.example.qasystem.user.service.IEmailService;
import com.example.qasystem.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//import javax.annotation.Resource;
import java.util.Collections;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class EmailServiceImpl implements IEmailService {
//    @Resource
//    private JavaMailSender mailSender;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;


    @Value("${spring.mail.email}")
    private String email;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    // 邮箱验证码过期时间
    @Value("${spring.mail.code.expiration}")
    private Long expiration;

    @Override
    public void sendEmail(EmailDto emailDto) {
        // 检查邮箱配置
        if (email == null || email.isEmpty() || host == null || host.isEmpty() || port == null || port.isEmpty() || username == null || password == null) {
            throw new RuntimeException("邮箱配置异常");
        }

        // 设置邮箱配置
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(host);
        mailAccount.setPort(Integer.parseInt(port));
        // 发送人邮箱
        mailAccount.setFrom(username + '<' + email + '>');
        // 发送人名称
        mailAccount.setUser(username);
        // 发送授权码
        mailAccount.setPass(password);
        mailAccount.setAuth(true);
        // ssl发送方式
        mailAccount.setSslEnable(true);
        // 使用安全连接
        mailAccount.setStarttlsEnable(true);

        // 发送邮件
        try {
            int size = emailDto.getTos().size();
            Mail.create(mailAccount)
                    .setTos(emailDto.getTos().toArray(new String[size]))
                    .setTitle(emailDto.getSubject())
                    .setContent(emailDto.getContent())
                    .setHtml(true)
                    // 关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public JsonResult sendEmailCode(String email) {
        JsonResult jsonResult = new JsonResult();
        // 校验邮箱是否已注册
        if (userService.registerEmailExist(email)) {
            return jsonResult.setCode(ResultCode.EMAIL_EXISTING).setSuccess(false).setMassage("该邮箱已注册");
        }

        // 从redis缓存中尝试获取验证码
        Object code = redisUtil.get(email);
        if (code == null) {
            // 如果缓存中未获取到验证码，生成6位验证码
            code = RandomUtil.randomNumbers(6);
            if (!redisUtil.set(email, code, expiration)) {
                throw new RuntimeException("后台缓存服务异常");
            }
        }

        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email-code.ftl");
        // 发送验证码
        sendEmail(new EmailDto(Collections.singletonList(email), "play-注册验证码", template.render(Dict.create().set("code", code))));

        return jsonResult;
    }
}
