package com.example.qasystem.user.service.impl;

import com.example.qasystem.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getOneFieldValueByUserId() {
        String s = (String) userService.getOneFieldValueByUserId(10L, User::getPhone);
        System.out.println(s);
    }
}