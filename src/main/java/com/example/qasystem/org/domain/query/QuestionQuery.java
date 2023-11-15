package com.example.qasystem.org.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQuery {
    private int page;
    private int pageSize;
    private String keywords;
}
