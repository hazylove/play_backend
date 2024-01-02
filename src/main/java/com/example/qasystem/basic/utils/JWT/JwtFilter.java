package com.example.qasystem.basic.utils.JWT;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);

            String userId = jwtUtil.getUserIdFromToken(authToken);
            Object tokenInRedis = redisTemplate.opsForValue().get("TOKEN_" + userId);
            if (tokenInRedis == null || !tokenInRedis.equals(authToken)) {
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                JSONObject result = new JSONObject();
                result.put("code", HttpStatus.UNAUTHORIZED.value());
                result.put("success", false);
                result.put("message", "Token验证失败或已过期，请重新登录！");
//                result.put("data", null);
//                result.put("resultObj", null);
                httpServletResponse.getWriter().write(result.toString());

                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                redisTemplate.opsForValue().set(userId, authToken, jwtUtil.getExpiration(), TimeUnit.SECONDS);
            }
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
