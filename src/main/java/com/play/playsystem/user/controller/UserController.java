package com.play.playsystem.user.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.user.domain.dto.ChangePasswordDto;
import com.play.playsystem.user.domain.dto.UserRegistrationDto;
import com.play.playsystem.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     * @param userRegistrationDto 注册信息
     * @return JsonResult
     */
    @PostMapping("/register")
    public JsonResult userRegister(@RequestBody UserRegistrationDto userRegistrationDto){
        return iUserService.register(userRegistrationDto);
    }

    @PostMapping("/changeAvatar")
    public JsonResult changeAvatar(@RequestParam MultipartFile avatarImage) throws IOException {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = Long.valueOf(authentication.getName());
            return iUserService.changeAvatar(userId, avatarImage);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
    }

    /**
     * 修改密码
     * @param changePasswordDto 密码信息
     * @return JsonResult
     */
    @PostMapping("/changePassword")
    public JsonResult changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = Long.valueOf(authentication.getName());
            changePasswordDto.setId(userId);
            // 修改密码
            return iUserService.changePassword(changePasswordDto);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
    }
}
