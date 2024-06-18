package com.play.playsystem.user.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.play.playsystem.basic.utils.tool.FormatCheckUtil;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.user.service.IEmailService;
import com.play.playsystem.user.service.IUserService;
import com.play.playsystem.user.domain.dto.EmailDto;
import com.play.playsystem.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class EmailServiceImpl implements IEmailService {

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
    public JsonResult sendRegisterCode(String email) {
        JsonResult jsonResult = new JsonResult();
        // 校验邮箱格式
        if (!FormatCheckUtil.validateEmail(email)) {
            return jsonResult.setCode(ResultCode.EMAIL_FORMAT_ERROR).setSuccess(false).setMessage("邮箱格式不正确");
        }
        // 校验邮箱是否已注册
        if (userService.registerEmailExist(email)) {
            return jsonResult.setCode(ResultCode.EMAIL_EXISTING).setSuccess(false).setMessage("该邮箱已注册");
        }

        // 生成并存储邮箱验证码
        Object code = generateAndSaveEmailCode(email);
        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("register-email-code.ftl");
        // 发送验证码
        sendEmail(new EmailDto(Collections.singletonList(email), "play-注册验证码", template.render(Dict.create().set("code", code))));

        return jsonResult;
    }

    @Override
    public JsonResult sendChangeCode(Long userId, String email) {
        JsonResult jsonResult = new JsonResult();
        Object userEmail = userService.getOneFieldValueByUserId(userId, User::getEmail);
        // 验证邮箱
        if (!Objects.equals(userEmail, email)){
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CHECK_FAILED).setMessage("邮箱验证失败");
        }

        // 生成并存储邮箱验证码
        Object code = generateAndSaveEmailCode(email);
        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("change-email-code.ftl");
        // 发送验证码
        sendEmail(new EmailDto(Collections.singletonList(email), "play-修改密码验证码", template.render(Dict.create().set("code", code))));

        return jsonResult;
    }

    @Override
    public JsonResult sendLoginCode(String email) {
        JsonResult jsonResult = new JsonResult();

        // 校验邮箱格式
        if (!FormatCheckUtil.validateEmail(email)) {
            return jsonResult.setCode(ResultCode.EMAIL_FORMAT_ERROR).setSuccess(false).setMessage("邮箱格式不正确");
        }
        // 校验邮箱是否已注册
        if (!userService.registerEmailExist(email)) {
            return jsonResult.setCode(ResultCode.EMAIL_NOT_REGISTER).setSuccess(false).setMessage("该邮箱未注册");
        }

        // 生成并存储邮箱验证码
        Object code = generateAndSaveEmailCode(email);
        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("login-email-code.ftl");
        // 发送验证码
        sendEmail(new EmailDto(Collections.singletonList(email), "play-登录验证码", template.render(Dict.create().set("code", code))));
        return jsonResult;
    }

    /**
     * 生成并在redis中存储邮箱验证码
     * @param email 邮箱
     * @return 验证码
     */
    private Object generateAndSaveEmailCode(String email) {
        // 从redis缓存中尝试获取验证码
        Object code = redisUtil.get(email);
        if (code == null) {
            // 如果缓存中未获取到验证码，生成6位验证码
            code = RandomUtil.randomNumbers(6);
            if (!redisUtil.set(email, code, expiration)) {
                throw new RuntimeException("后台缓存服务异常");
            }
        }
        return code;
    }
}
