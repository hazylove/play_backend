package com.example.qasystem.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    // 发送邮箱列表
    private List<String> tos;
    // 主体
    private String subject;
    //内容
    private String content;
}
