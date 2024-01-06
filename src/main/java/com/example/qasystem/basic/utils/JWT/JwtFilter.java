package com.example.qasystem.basic.utils.JWT;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

    @Value("${jwt.ignore-paths:}")
    private String[] ignorePaths;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    /**
     * 忽略请求
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String ignorePath : ignorePaths) {
            if (pathMatcher.match(ignorePath, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  验证token并设置用户信息到SecurityContextHolder中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader("Authorization");
        if (authToken == null){
            sendUnauthorizedResponse(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), "请求未携带token！");
            return;
        }

        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);

            String userId = jwtUtil.getUserIdFromToken(authToken);
            Object tokenInRedis = redisTemplate.opsForValue().get("TOKEN_" + userId);
            if (tokenInRedis == null || !tokenInRedis.equals(authToken)) {
                sendUnauthorizedResponse(httpServletResponse,  HttpStatus.UNAUTHORIZED.value(), "Token验证失败或已过期，请重新登录！");
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

    public static void sendUnauthorizedResponse(HttpServletResponse httpServletResponse, int code, String message) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("success", false);
        result.put("message", message);
        httpServletResponse.getWriter().write(result.toString());
    }
}
