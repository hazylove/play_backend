package com.example.qasystem.user.security.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {


        JsonResult result = new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setMassage(accessDeniedException.getMessage()).setSuccess(false);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        response.getWriter().println(JSON.toJSON(result));
    }
}
