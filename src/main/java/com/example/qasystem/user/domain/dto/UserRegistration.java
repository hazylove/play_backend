package com.example.qasystem.user.domain.dto;

import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRegistration extends UserBase {
    private String password1;
    private String password2;
    private String phone;
    private String email;
    private Date createdDate;
}
