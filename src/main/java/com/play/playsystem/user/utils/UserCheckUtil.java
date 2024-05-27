package com.play.playsystem.user.utils;

import com.play.playsystem.basic.utils.tool.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserCheckUtil {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户名为空检查
     * @param username 用户名
     * @return 是否为空
     */
    public boolean isInvalidUsername(String username) {
        return username == null || username.isEmpty();
    }

    /**
     * 邮箱验证码校验
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     * @return 成功返回false，失败返回true
     */
    public boolean isInvalidEmailCode(String email, String emailCode) {
        String redisEmailCode = redisUtil.get(email);
        return !Objects.equals(redisEmailCode, emailCode);
    }
}
