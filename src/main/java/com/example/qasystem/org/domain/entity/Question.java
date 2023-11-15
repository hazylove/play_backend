package com.example.qasystem.org.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Long id;
    private String questionTitle;
    private String questionTag;
    private Long questionCreatedId;
    private User questionCreatedBy;
    private Date questionCreatedDate;
}
