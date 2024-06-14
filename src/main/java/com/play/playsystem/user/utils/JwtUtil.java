package com.play.playsystem.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;


@Getter
@Component
public class JwtUtil {

    // 过期时间
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.secret}")
    private String secret;


    /**
     * 生成token
     * @param username 用户名
     * @return token
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        //Date expireDate = new Date(nowDate.getTime() + 1000 * expiration);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                //.setExpiration(expireDate)    // 过期时间
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析Token
     * @param token token
     * @return 用户信息
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断token是否过期
     * @param claims 认证信息
     * @return 是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


}