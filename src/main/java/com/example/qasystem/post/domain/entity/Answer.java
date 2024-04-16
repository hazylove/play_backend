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
@Alias("Answer")
public class Answer {
    private Long id;
    private String answerContent;
    private Long answerQuestionId;
    private Long answerCreatedId;
    private User answerCreatedBy;
    private Date answerCreatedDate;
}
