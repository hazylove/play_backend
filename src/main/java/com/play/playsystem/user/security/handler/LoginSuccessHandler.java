package com.play.playsystem.user.security.handler;

import com.alibaba.fastjson.JSON;
import com.play.playsystem.user.utils.JwtUtil;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.basic.utils.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.play.playsystem.basic.constant.AuthConstant.TOKEN_REDIS_PREFIX;

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
        JsonResult result = new JsonResult().setData(token).setMessage("登录成功");

        // 将token存入redis，并设置过期时间
        redisUtil.set(TOKEN_REDIS_PREFIX + authentication.getName(), token, jwtUtil.getExpiration());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSON(result));
    }
}
