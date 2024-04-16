package com.example.qasystem.user.domain.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("User")
public class User {
    private Long id;
    private String username;
    private String password;
}
