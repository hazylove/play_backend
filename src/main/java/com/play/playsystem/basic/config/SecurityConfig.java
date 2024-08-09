package com.play.playsystem.basic.config;

import com.play.playsystem.user.security.email.EmailCodeAuthenticationFilter;
import com.play.playsystem.user.security.email.EmailCodeAuthenticationProvider;
import com.play.playsystem.user.security.filter.CaptchaFilter;
import com.play.playsystem.user.security.filter.JsonAuthenticationFilter;
import com.play.playsystem.user.security.filter.JwtAuthenticationFilter;
import com.play.playsystem.user.security.handler.*;
import com.play.playsystem.user.service.impl.UserDetailsServiceImpl;
import com.play.playsystem.user.utils.PasswordEncoder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

//                .csrf().disable().authorizeRequests()
//                .and().authorizeRequests().anyRequest().permitAll()
//                .and().logout().permitAll()
//                .and().build();

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
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
    private LogoutHandler logoutHandler;

    @Autowired
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 允许访问的地址，由yml中配置
    private String[] ignorePaths;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration corsConfig = new CorsConfiguration();
                            corsConfig.setAllowedOrigins(Collections.singletonList("*"));
                            corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                            return corsConfig;
                        })
                ).csrf().disable()

                //禁用session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers("/ws/**").permitAll()
                .antMatchers(ignorePaths).permitAll()
                .anyRequest()
                .authenticated()

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //注销配置
                .and()
                .logout()
                .logoutUrl(apiPrefix + "/user/logout") // 设置注销 URL
                .logoutSuccessHandler(logoutHandler)

                //配置自定义登录拦截及配置 jsonAuthenticationFilter 替换 UsernamePasswordAuthenticationFilter
                .and()
                .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 邮箱验证码登录过滤器
                .authenticationProvider(emailCodeAuthenticationProvider)
                .addFilterBefore(emailCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                //配置jwt过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                //验证码过滤器
                .addFilterBefore(captchaFilter, JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ws/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    protected AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(emailCodeAuthenticationProvider,daoAuthenticationProvider()));
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }


    /**
     * 邮箱验证码登录过滤器
     * @return 过滤器
     */
    @Bean
    public EmailCodeAuthenticationFilter emailCodeAuthenticationFilter() {
        EmailCodeAuthenticationFilter filter = new EmailCodeAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        // 自定义邮箱登录验证地址
        filter.setFilterProcessesUrl(apiPrefix + "/user/emailLogin");
        return filter;
    }

    /**
     * json账号密码登录过滤器
     * @return 过滤器
     */
    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        // 自定义登录接口请求路径
        filter.setFilterProcessesUrl(apiPrefix + "/user/login");
        filter.setPostOnly(true);
        return filter;
    }
}
