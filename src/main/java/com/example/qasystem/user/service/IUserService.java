package com.example.qasystem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.domain.entity.User;

public interface IUserService extends IService<User> {
    /**
     * 注册
     * @param userRegistration 注册用户信息
     * @return 注册结果
     */
    JsonResult register(UserRegistration userRegistration);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 检查邮箱是否已注册
     * @param email 用户名
     * @return 是否注册
     */
    boolean registerEmailExist(String email);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean registerUsernameExist(String username);
}
