package com.example.qasystem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.user.domain.dto.UserRegistration;
import com.example.qasystem.user.domain.entity.User;

public interface IUserService extends IService<User> {
    int register(UserRegistration userRegistration);

    User getUserByUsername(String username);

    boolean registerEmailExist(String email);
}
