package com.example.qasystem.user.domain.entity.base;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserBase {
    @Id
    private Long id;
    private String username;
}
