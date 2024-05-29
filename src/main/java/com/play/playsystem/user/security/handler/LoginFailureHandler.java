package com.play.playsystem.user.security.handler;

import com.alibaba.fastjson.JSON;
import com.play.playsystem.basic.exception.CaptchaException;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        JsonResult result = new JsonResult().setSuccess(false);
        if (exception.getCause() instanceof CaptchaException) {
            result.setCode(ResultCode.VERIFICATION_CODE_ERROR_CODE).setMassage("验证码错误");
        } else {
            result.setCode(ResultCode.LOGIN_FAILED).setMassage(exception.getLocalizedMessage());
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSON(result));
    }
}
