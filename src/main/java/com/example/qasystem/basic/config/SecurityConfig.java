package com.example.qasystem.basic.config;

import com.example.qasystem.user.security.filter.CaptchaFilter;
import com.example.qasystem.user.security.filter.JsonAuthenticationFilter;
import com.example.qasystem.user.security.filter.JwtAuthenticationFilter;
import com.example.qasystem.user.security.handler.*;
import com.example.qasystem.user.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsManager;

    @Autowired
    private LogoutHandler logoutHandler;


    @Value("${jwt.ignore-paths}")
    private final String[] URL_WHITELIST = new String[]{"/login", "/logout", "/register"};

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                //登录配置
                //配置自定义登录拦截及配置 jsonAuthenticationFilter 替换 UsernamePasswordAuthenticationFilter
                .addFilterAt(jsonAuthenticationFilter(http), UsernamePasswordAuthenticationFilter.class)

                //注销配置
                .logout()
                .logoutUrl(apiPrefix + "/logout") // 设置注销 URL
                .logoutSuccessHandler(logoutHandler)

                //禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest()
                .authenticated()

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //配置jwt过滤器
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                //验证码过滤器
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter(HttpSecurity http) throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        // 可自定义登录接口请求路径
        filter.setFilterProcessesUrl(apiPrefix + "/login");
        filter.setAuthenticationManager(authenticationManager(http));
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.
                getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsManager;
    }
}
