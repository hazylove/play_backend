package com.example.qasystem.org.service;

import com.example.qasystem.org.domain.dto.UserLogin;
import com.example.qasystem.org.domain.dto.UserRegistration;

public interface IUserService {
    int register(UserRegistration userRegistration);

    boolean login(UserLogin userLogin);
}
