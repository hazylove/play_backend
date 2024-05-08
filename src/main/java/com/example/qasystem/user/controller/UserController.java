package com.example.qasystem.user.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}")
@Slf4j
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
        }catch (Exception e){
            log.error("用户注册出现异常：", e);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("服务器异常");
        }
    }

}
