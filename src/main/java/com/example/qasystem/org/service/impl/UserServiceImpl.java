package com.example.qasystem.org.service.impl;

import com.example.qasystem.basic.utils.JWT.JwtUtil;
import com.example.qasystem.org.domain.dto.UserLogin;
import com.example.qasystem.org.domain.dto.UserRegistration;
import com.example.qasystem.org.domain.entity.User;
import com.example.qasystem.org.mapper.UserMapper;
import com.example.qasystem.org.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public int register(UserRegistration userRegistration) {
        // 校验输入格式
        if (isInvalidUsername(userRegistration.getUsername()) || isInvalidPassword(userRegistration.getPassword1())) {
            return -2;
        }
        // 检查两次输入密码
        if (!Objects.equals(userRegistration.getPassword1(), userRegistration.getPassword2())){
            return 0;
        }
        // 检查用户名是否存在
        Long userId =userMapper.getUserIdByUsername(userRegistration.getUsername());
        if (userId != null){
            return -1;
        }
        // 注册
        userRegistration.setCreatedDate(Calendar.getInstance().getTime());
        // 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userRegistration.getPassword1());
        userRegistration.setPassword1(hashedPassword);
        userMapper.insert(userRegistration);
        return 1;
    }

    /**
     * 登录业务
     * @param userLogin 用户信息
     * @return token
     */
    @Override
    public String login(UserLogin userLogin) {
        // 校验验证码

        // 查询用户
        User user = userMapper.getUserByUsername(userLogin.getUsername());
        // 加密校验操作
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密校验操作
        if (user != null && passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getId());
            redisTemplate.opsForValue().set("TOKEN_" + user.getId(), token, jwtUtil.getExpiration(), TimeUnit.SECONDS);
            return token;
        }else {
            return null;
        }
    }

    // 用户名为空检查
    private boolean isInvalidUsername(String username) {
        return username == null || username.isEmpty();
    }

    // 密码格式检查
    private boolean isInvalidPassword(String password) {
        final int MIN_PASSWORD_LENGTH = 6;
        final int MAX_PASSWORD_LENGTH = 18;
        return password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH;
    }


}
