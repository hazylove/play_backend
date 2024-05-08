package com.example.qasystem.user.security.handler;

import com.alibaba.fastjson.JSON;
import com.example.qasystem.basic.utils.JWT.JwtUtil;
import com.example.qasystem.basic.utils.RedisUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.qasystem.basic.constant.AuthConstant.TOKEN_KEY_PREFIX;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 根据用户名生成token
        String token = jwtUtil.generateToken(authentication.getName());
        JsonResult result = new JsonResult().setData(token).setMassage("登录成功");

        // 将token存入redis，并设置过期时间
        redisUtil.set(TOKEN_KEY_PREFIX + authentication.getName(), token, jwtUtil.getExpiration());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSON(result));
    }
}
