package com.play.playsystem.user.utils;

import cn.hutool.core.util.StrUtil;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.play.playsystem.basic.constant.AuthConstant.TOKEN_REDIS_PREFIX;

@Slf4j
@Component
public class UserUtil {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户名为空检查
     * @param username 用户名
     * @return 是否为空
     */
    public static boolean isInvalidUsername(String username) {
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

    public static boolean checkAuth(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser");
    }

    /**
     * 根据token解析用户ID
     * @param token token
     * @return 用户ID
     */
    public Long extractUserIdFromToken(String token) {
        if (StrUtil.isBlankOrUndefined(token)) {
            return null;
        }

        Claims claims = jwtUtil.getClaimsByToken(token);
        if (claims == null) {
            log.error("无效token: {}", token);
            return null;
        }

        Long userId = Long.valueOf(claims.getSubject());
        Object storedToken = redisUtil.get(TOKEN_REDIS_PREFIX + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            log.error("用户id {} 的令牌失效: {}", userId, token);
            return null;
        }

        return userId;
    }
}
