package com.example.qasystem.user.utils;

import com.example.qasystem.basic.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserCheckUtil {
    @Autowired
    private RedisUtil redisUtil;

    // 用户名为空检查
    public boolean isInvalidUsername(String username) {
        return username == null || username.isEmpty();
    }

    // 密码格式检查
    public boolean isInvalidPassword(String password) {
        final int MIN_PASSWORD_LENGTH = 6;
        final int MAX_PASSWORD_LENGTH = 18;
        return password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH;
    }

    // 邮箱验证码检查
    public boolean isInvalidEmailCode(String email, String emailCode) {
        String redisEmailCode = redisUtil.get(email);
        return Objects.equals(redisEmailCode, emailCode);
    }
}
