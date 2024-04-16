package com.example.qasystem.basic.config;

import com.example.qasystem.basic.utils.JWT.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    /**
     * 配置哪些请求不拦截
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(apiPrefix + "/users/login", apiPrefix + "/users/register");
    }

    /**
     * 配置安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(apiPrefix +"/users/login").permitAll()
                .antMatchers( apiPrefix + "/users/register").permitAll()
                .anyRequest()
                .authenticated();
    }

}
