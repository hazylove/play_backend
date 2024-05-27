package com.play.playsystem.user.security.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        JsonResult result = new JsonResult().setCode(ResultCode.UNAUTHORIZED_CODE).setMassage("请先登录").setSuccess(false);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        response.getWriter().println(JSON.toJSON(result));
    }
}
