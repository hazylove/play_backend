package com.play.playsystem.user.security.email;

import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.basic.utils.tool.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * 邮件验证过滤器
 */
public class EmailCodeAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {

    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    private static final String DEFAULT_EMAIL_NAME="email";
    private static final String DEFAULT_EMAIL_CODE="emailCode";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * 通过 传入的 参数 创建 匹配器
     * 即 Filter过滤的url
     */
    public EmailCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/user/emailLogin","POST"));
    }


    /**
     * 给权限
     * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到 token 上 ，
     * 然后把token 交给 provider 进行授权
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*
          是否 仅仅post
         */
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }else{
            // 邮箱
            String email;
            // 邮箱验证码
            String emailCode;

            // 通过 ContentType 判断是否为 json 格式数据
            String contentType = request.getContentType();
            if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType) || APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType.trim())) {
                // 获取请求体中的参数
                Map<String, Object> requestBody = RequestUtil.getBodyParametersFromRequest(request);
                email = (String) requestBody.get(DEFAULT_EMAIL_NAME);
                emailCode = (String) requestBody.get(DEFAULT_EMAIL_CODE);
            } else {
                // 获取表单中的参数
                email = request.getParameter(DEFAULT_EMAIL_NAME);
                emailCode = request.getParameter(DEFAULT_EMAIL_CODE);
            }

            // 封装 token
            EmailCodeAuthenticationToken token;
            // 如果 验证码不相等 故意让token出错 然后走SpringSecurity 错误的流程
            boolean flag = emailCode.equals(redisUtil.get(email));
            if (flag) {
                token  = new EmailCodeAuthenticationToken(email,new ArrayList<>());
                // 验证成功，删除redis中的验证码
                redisUtil.del(email);
            } else {
                token = new EmailCodeAuthenticationToken("error");
            }
            setDetails(request,token);
            //交给 AuthenticationManager 进行认证 这个流程中在代码中有说明
            return this.getAuthenticationManager().authenticate(token);
        }
    }

    public void setDetails(HttpServletRequest request , EmailCodeAuthenticationToken token ){
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}