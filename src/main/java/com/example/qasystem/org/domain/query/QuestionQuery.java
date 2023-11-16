package com.example.qasystem.org.domain.query;

import com.example.qasystem.basic.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQuery extends PageQuery {
    private String keywords;
}
