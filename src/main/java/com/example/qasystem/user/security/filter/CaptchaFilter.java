package com.example.qasystem.user.security.filter;

import com.example.qasystem.basic.exception.CaptchaException;
import com.example.qasystem.basic.utils.tool.RequestUtil;
import com.example.qasystem.user.security.handler.LoginFailureHandler;
import com.example.qasystem.basic.constant.AuthConstant;
import com.example.qasystem.basic.utils.tool.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ((apiPrefix + "/login").equals(url) && request.getMethod().equals("POST")) {
            // 校验验证码
            try {
                validate(request);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {
        Map<String, Object> bodyParametersFromRequest = RequestUtil.getBodyParametersFromRequest(httpServletRequest);

        String code = (String) bodyParametersFromRequest.get("code");
        String key = (String) bodyParametersFromRequest.get("userKey");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtil.hget(AuthConstant.CAPTCHA_REDIS_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 若验证码正确，执行以下语句
        // 一次性使用
        redisUtil.hdel(AuthConstant.CAPTCHA_REDIS_KEY, key);
    }
}

