package com.example.qasystem.basic.config;

import com.example.qasystem.basic.utils.JWT.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig{

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    /**
     * 配置安全策略
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorize -> authorize
                        .antMatchers(apiPrefix + "/users/login").permitAll()
                        .antMatchers(apiPrefix + "/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    /**
     * 配置哪些请求不拦截
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 仅仅作为演示
        return (web) -> web.ignoring().antMatchers(apiPrefix + "/users/login", apiPrefix + "/users/register", apiPrefix + "/questions/hello");
    }

}
