package com.example.qasystem.org.mapper;

import com.example.qasystem.org.domain.dto.UserRegistration;
import com.example.qasystem.org.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Long getUserIdByUsername(String username);

    void insert(UserRegistration userRegistration);

    String getPasswordByUsername(String username);

    User getUserByUsername(String username);
}