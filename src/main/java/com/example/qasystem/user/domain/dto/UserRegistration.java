package com.example.qasystem.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
    private Long id;
    private String username;
    private String password1;
    private String password2;
    private String email;
    private String phone;
    private Date createdDate;
}
