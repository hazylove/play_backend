package com.play.playsystem.user.security.filter;

import com.play.playsystem.basic.utils.tool.RequestUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //通过 ContentType 判断是否为 json 格式数据
        String contentType = request.getContentType();
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType) || APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType.trim())) {
            // 获取请求体中的参数
            Map<String, Object> user = RequestUtil.getBodyParametersFromRequest(request);
            //使用用户名密码构建 UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.get(USERNAME), user.get(PASSWORD));
            setDetails(request, authenticationToken);

            return this.getAuthenticationManager().authenticate(authenticationToken);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
