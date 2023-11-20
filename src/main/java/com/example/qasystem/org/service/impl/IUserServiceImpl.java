package com.example.qasystem.org.service.impl;

import com.example.qasystem.org.domain.dto.UserLogin;
import com.example.qasystem.org.domain.dto.UserRegistration;
import com.example.qasystem.org.mapper.UserMapper;
import com.example.qasystem.org.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

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

    @Override
    public boolean login(UserLogin userLogin) {
        // 校验验证码

        // 查询密码
        String password = userMapper.getPasswordByUsername(userLogin.getUsername());
        // 加密校验操作
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密校验操作
        return passwordEncoder.matches(userLogin.getPassword(), password);
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
