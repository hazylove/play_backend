package com.example.qasystem.org.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.org.domain.dto.UserLogin;
import com.example.qasystem.org.domain.dto.UserRegistration;
import com.example.qasystem.org.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public JsonResult userRegister(@RequestBody UserRegistration userRegistration){
        int result = iUserService.register(userRegistration);
        JsonResult jsonResult = new JsonResult();
        if (result == -2){
            jsonResult.setSuccess(false).setMassage("用户名、密码格式不正确");
        }
        else if (result == 0){
            jsonResult.setSuccess(false).setMassage("两次输入密码不一致");
        }
        else if (result == -1){
            jsonResult.setSuccess(false).setMassage("用户名已存在");
        }
        else {
            jsonResult.setMassage("注册成功");
        }
        return jsonResult;
    }

    @PostMapping("/login")
    public JsonResult userLogin(@RequestBody UserLogin userLogin){
        String token = iUserService.login(userLogin);
        JsonResult jsonResult = new JsonResult();
        if (token != null){
            jsonResult.setData(token).setMassage("登录成功");
        }
        else {
            jsonResult.setSuccess(false).setMassage("用户名或密码错误，登录失败");
        }
        return jsonResult;
    }


}
