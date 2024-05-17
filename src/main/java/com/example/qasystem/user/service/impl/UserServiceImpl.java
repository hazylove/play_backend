package com.example.qasystem.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qasystem.basic.utils.FormatCheckUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.domain.entity.User;
import com.example.qasystem.user.mapper.UserMapper;
import com.example.qasystem.user.utils.PasswordEncoder;
import com.example.qasystem.user.service.IUserService;
import com.example.qasystem.user.utils.UserCheckUtil;
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

    @Autowired
    private UserCheckUtil userCheckUtil;

    @Autowired
    private FormatCheckUtil formatCheckUtil;


    @Override
    @Transactional
    public JsonResult register(UserRegistration userRegistration) {
        JsonResult jsonResult = new JsonResult();
        // 校验用户名格式
        if (userCheckUtil.isInvalidUsername(userRegistration.getUsername()) && formatCheckUtil.validateEmail(userRegistration.getUsername())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_PASSWORD_FORMAT_ERROR).setMassage("用户名格式不正确");
        }
        // 校验手机号格式
        if (userRegistration.getPhone() !=  null && !userRegistration.getPhone().isEmpty() && !formatCheckUtil.validatePhone(userRegistration.getPhone())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.PHONE_FORMAT_ERROR).setMassage("手机号格式不正确");
        }
        // 检查两次输入密码
        if (!Objects.equals(userRegistration.getPassword1(), userRegistration.getPassword2())){
            return jsonResult.setSuccess(false).setCode(ResultCode.TWICE_PASSWORD_INCONSISTENT).setMassage("两次输入密码不一致");
        }
        // 检查用户名是否存在
        if (registerUsernameExist(userRegistration.getUsername())){
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_EXISTING).setMassage("用户名已存在");
        }
        // 检查邮箱是否已注册
        if (registerEmailExist(userRegistration.getEmail())) {
            return new JsonResult().setCode(ResultCode.EMAIL_EXISTING).setSuccess(false).setMassage("该邮箱已注册");
        }
        // 校验邮箱验证码
        if (!userCheckUtil.isInvalidEmailCode(userRegistration.getEmail(), userRegistration.getEmailCode())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CODE_ERROR).setMassage("邮箱验证码错误");
        }

        // 创建用户
        User user = new User(
                null, // 自增id
                userRegistration.getUsername(), //用户名
                passwordEncoder.encode(userRegistration.getPassword1()), // 加密后的密码
                userRegistration.getEmail(), // 邮箱
                userRegistration.getPhone(), // 电话
                Calendar.getInstance().getTime() // 注册时间
        );
        userMapper.insert(user);

        return jsonResult.setMassage("注册成功");
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsernameOrEmail(String Account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", Account).or().eq("email", Account);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean registerEmailExist(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public boolean registerUsernameExist(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper) != null;
    }

}
