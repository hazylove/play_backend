package com.example.qasystem.user.controller;


import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.user.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/sendCode")
    public JsonResult sendEmailCode(@RequestParam String email) {
        return emailService.sendEmailCode(email);
    }
}
