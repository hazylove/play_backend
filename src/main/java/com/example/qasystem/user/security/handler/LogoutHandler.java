package com.example.qasystem.user.security.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.example.qasystem.basic.utils.JWT.JwtUtil;
import com.example.qasystem.basic.utils.RedisUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.qasystem.basic.constant.AuthConstant.TOKEN_KEY_PREFIX;
import static com.example.qasystem.basic.constant.AuthConstant.TOKEN_PREFIX;
import static com.example.qasystem.basic.utils.result.ResultCode.SUCCESS_CODE;

@Component
public class LogoutHandler implements LogoutSuccessHandler {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        String token = request.getHeader(jwtUtil.getHeader());
        if (!StrUtil.isBlankOrUndefined(token) && token.startsWith(TOKEN_PREFIX)) {
            String cleanedToken = token.substring(7);
            Claims claim = jwtUtil.getClaimsByToken(cleanedToken);
            if (claim != null) {
                String username = claim.getSubject();
                redisUtil.del(TOKEN_KEY_PREFIX + username);
            }
        }

        JsonResult result = new JsonResult().setMassage("退出成功");

        response.setHeader(jwtUtil.getHeader(), "");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSON(result));
    }
}
