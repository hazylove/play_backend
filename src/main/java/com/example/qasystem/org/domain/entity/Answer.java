package com.example.qasystem.org.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private Long id;
    private String answerContent;
    private Long answerCreatedId;
    private User answerCreatedBy;
    private Date answerCreatedDate;
}
