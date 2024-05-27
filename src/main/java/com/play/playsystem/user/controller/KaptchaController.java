package com.play.playsystem.user.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.user.service.IKaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/captcha")
@Slf4j
public class KaptchaController {

    @Autowired
    private IKaptchaService kaptchaService;

    @GetMapping("/create")
    public JsonResult createCaptcha() throws IOException {
        return kaptchaService.createCaptcha();
    }
}
