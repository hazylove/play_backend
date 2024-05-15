package com.example.qasystem.user.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     * @param userRegistration 注册信息
     * @return JsonResult
     */
    @PostMapping("/register")
    public JsonResult userRegister(@RequestBody UserRegistration userRegistration){
        try {
            return iUserService.register(userRegistration);
        }catch (Exception e){
            log.error("用户注册出现异常：", e);
            return new JsonResult().setCode(ResultCode.ERROR_CODE).setSuccess(false).setMassage("服务器异常");
        }
    }

}
