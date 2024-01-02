package com.example.qasystem.basic.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class JwtUtil {

    // 过期时间
    @Value("${jwt.expiration}")
    private Long expiration;

    // key
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成token
     * @param userId 用户ID
     * @return token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId);
        claims.put("iat", now);
        claims.put("exp", expiryDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从token中获取用户id
     * @param token token
     * @return userId
     * @throws JwtException 如果解析 token 出现问题，会抛出该异常
     */
    public String getUserIdFromToken(String token) throws JwtException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (SecurityException e) {
            // 可以根据具体情况返回特定的错误码或消息给调用方
            throw new JwtException("解析token令牌失败: " + e.getMessage());
        }
    }


}