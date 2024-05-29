package com.play.playsystem.user.security.email;

import com.play.playsystem.user.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 邮件验证过滤器
 */
@Slf4j
@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        log.info("EmailCodeAuthentication authentication request: {}", authentication);
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;

        // 构建 UserDetails
        UserDetails userDetails = userDetailsService.loadUserByEmail((String) token.getPrincipal());

        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        /*
        Details 中包含了 ip地址、 sessionId 等等属性
        其实还可以存储一些我们想要存储的数据，之后我们再利用。、
        */
        result.setDetails(token.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}