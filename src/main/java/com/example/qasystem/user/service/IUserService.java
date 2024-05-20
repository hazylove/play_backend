package com.example.qasystem.user.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.user.domain.dto.ChangePasswordDto;
import com.example.qasystem.user.domain.dto.UserRegistrationDto;
import com.example.qasystem.user.domain.entity.User;

public interface IUserService extends IService<User> {
    /**
     * 注册
     * @param userRegistrationDto 注册用户信息
     * @return 注册结果
     */
    JsonResult register(UserRegistrationDto userRegistrationDto);

    /**
     * 修改密码
     * @param changePasswordDto 修改密码信息
     * @return 操作结果
     */
    JsonResult changePassword(ChangePasswordDto changePasswordDto);

    /**
     * 根据id查用户
     * @param id 用户id
     * @return 用户
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 根据用户名或邮箱查询用户
     * @param Account 账号：用户名或邮箱
     * @return 用户
     */
    User getUserByUsernameOrEmail(String Account);

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

    /**
     * 根据用户id查询单个用户单个字段的值
     * @param userId 用户id
     * @param fieldExtractor 查询字段的 lambda表达式
     * @return 值
     */
    Object getOneFieldValueByUserId(Long userId, SFunction<User, String> fieldExtractor);
}
