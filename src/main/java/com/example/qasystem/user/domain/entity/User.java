package com.example.qasystem.user.domain.entity;

import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Alias("User")
public class User extends UserBase {

    private String password;
    private String email;
    private String phone;
}
