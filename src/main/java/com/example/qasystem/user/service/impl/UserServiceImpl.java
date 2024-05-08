package com.example.qasystem.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.domain.entity.User;
import com.example.qasystem.user.mapper.UserMapper;
import com.example.qasystem.user.security.utils.PasswordEncoder;
import com.example.qasystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String hashedPassword = passwordEncoder.encode(userRegistration.getPassword1());
        userRegistration.setPassword1(hashedPassword);
        userMapper.insert(userRegistration);
        return 1;
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
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
