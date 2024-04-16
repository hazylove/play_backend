package com.example.qasystem.user.service;

import com.example.qasystem.user.domain.dto.UserLogin;
import com.example.qasystem.user.domain.dto.UserRegistration;

public interface IUserService {
    int register(UserRegistration userRegistration);

    String login(UserLogin userLogin);
}
