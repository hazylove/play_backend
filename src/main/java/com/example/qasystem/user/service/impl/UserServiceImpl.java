package com.example.qasystem.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qasystem.basic.constant.AuthConstant;
import com.example.qasystem.basic.utils.FormatCheckUtil;
import com.example.qasystem.basic.utils.RedisUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.user.domain.dto.ChangePasswordDto;
import com.example.qasystem.user.domain.dto.UserRegistrationDto;
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

    @Autowired
    private RedisUtil redisUtil;


    @Override
    @Transactional
    public JsonResult register(UserRegistrationDto userRegistrationDto) {
        JsonResult jsonResult = new JsonResult();
        // 校验用户名格式
        if (userCheckUtil.isInvalidUsername(userRegistrationDto.getUsername()) && formatCheckUtil.validateEmail(userRegistrationDto.getUsername())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_PASSWORD_FORMAT_ERROR).setMassage("用户名格式不正确");
        }
        // 校验手机号格式
        if (userRegistrationDto.getPhone() !=  null && !userRegistrationDto.getPhone().isEmpty() && !formatCheckUtil.validatePhone(userRegistrationDto.getPhone())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.PHONE_FORMAT_ERROR).setMassage("手机号格式不正确");
        }
        // 检查两次输入密码
        if (!Objects.equals(userRegistrationDto.getPassword1(), userRegistrationDto.getPassword2())){
            return jsonResult.setSuccess(false).setCode(ResultCode.TWICE_PASSWORD_INCONSISTENT).setMassage("两次输入密码不一致");
        }
        // 检查用户名是否存在
        if (registerUsernameExist(userRegistrationDto.getUsername())){
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_EXISTING).setMassage("用户名已存在");
        }
        // 检查邮箱是否已注册
        if (registerEmailExist(userRegistrationDto.getEmail())) {
            return new JsonResult().setCode(ResultCode.EMAIL_EXISTING).setSuccess(false).setMassage("该邮箱已注册");
        }
        // 校验邮箱验证码
        if (userCheckUtil.isInvalidEmailCode(userRegistrationDto.getEmail(), userRegistrationDto.getEmailCode())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CODE_ERROR).setMassage("邮箱验证码错误");
        }

        // 创建用户
        User user = new User(
                null, // 自增id
                userRegistrationDto.getUsername(), //用户名
                passwordEncoder.encode(userRegistrationDto.getPassword1()), // 加密后的密码
                userRegistrationDto.getEmail(), // 邮箱
                userRegistrationDto.getPhone(), // 电话
                Calendar.getInstance().getTime() // 注册时间
        );
        userMapper.insert(user);

        return jsonResult.setMassage("注册成功");
    }

    @Override
    @Transactional
    public JsonResult changePassword(ChangePasswordDto changePasswordDto) {
        JsonResult jsonResult = new JsonResult();
        String email = (String) getOneFieldValueByUserId(changePasswordDto.getId(), User::getEmail);
        // 检查两次输入密码
        if (!Objects.equals(changePasswordDto.getNewPassword1(), changePasswordDto.getNewPassword2())){
            return jsonResult.setSuccess(false).setCode(ResultCode.TWICE_PASSWORD_INCONSISTENT).setMassage("两次输入密码不一致");
        }
        // 校验邮箱验证码
        if (userCheckUtil.isInvalidEmailCode(email, changePasswordDto.getEmailCode())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CODE_ERROR).setMassage("邮箱验证码错误");
        }
        // 修改密码
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(User::getPassword, passwordEncoder.encode(changePasswordDto.getNewPassword1()))
                .eq(User::getId, changePasswordDto.getId());
        userMapper.update(null, updateWrapper);

        // 删除Redis中存储的token
        String username = (String) getOneFieldValueByUserId(changePasswordDto.getId(), User::getUsername);
        redisUtil.del(AuthConstant.TOKEN_REDIS_PREFIX + username, email);

        return jsonResult.setMassage("修改成功");
    }

    @Override
    public User getUserById(Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, id);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsernameOrEmail(String Account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, Account).or().eq(User::getEmail, Account);
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

    @Override
    public Object getOneFieldValueByUserId(Long userId, SFunction<User, String> fieldExtractor) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(fieldExtractor).eq(User::getId, userId);
        return userMapper.selectObjs(queryWrapper).stream().findFirst().orElse(null);
    }
}
