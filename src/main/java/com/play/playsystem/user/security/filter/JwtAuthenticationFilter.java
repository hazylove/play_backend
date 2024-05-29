package com.play.playsystem.user.security.filter;

import cn.hutool.core.util.StrUtil;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.user.service.impl.UserDetailsServiceImpl;
import com.play.playsystem.user.utils.JwtUtil;
import com.play.playsystem.user.domain.entity.User;
import com.play.playsystem.user.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.play.playsystem.basic.constant.AuthConstant.TOKEN_REDIS_PREFIX;
import static com.play.playsystem.basic.constant.AuthConstant.TOKEN_PREFIX;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        String token = request.getHeader(jwtUtil.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (StrUtil.isBlankOrUndefined(token) || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        String cleanedToken = token.substring(7);
        Claims claim = jwtUtil.getClaimsByToken(cleanedToken);
        if (claim == null) {
            throw new JwtException("token 异常");
        }

        String username = claim.getSubject();

        Object tokenInRedis = redisUtil.get(TOKEN_REDIS_PREFIX + username);
        if (tokenInRedis == null || !tokenInRedis.equals(cleanedToken)) {
            throw new JwtException("token 异常");
        }

        // 获取用户的权限等信息
        User user = userService.getUserByUsername(username);

        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), null, userDetailsService.getUserAuthority(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 刷新token过期时间
        redisUtil.expire(TOKEN_REDIS_PREFIX + username, jwtUtil.getExpiration());

        chain.doFilter(request, response);
    }
}
