package com.example.qasystem.user.domain.entity.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserBase {
    private Long id;
    private String username;
}
