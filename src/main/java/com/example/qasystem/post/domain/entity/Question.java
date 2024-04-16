package com.example.qasystem.post.domain.entity;

import com.example.qasystem.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Question")
public class Question {
    private Long id;
    private String questionTitle;
    private String questionContent;
    private String questionTag;
    private Long questionCreatedId;
    private User questionCreatedBy;
    private Date questionCreatedDate;
    private int questionAnswerNum;
}
