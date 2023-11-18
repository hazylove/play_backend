package com.example.qasystem.org.domain.query;

import com.example.qasystem.basic.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuery extends PageQuery {
    private Long questionId;
    private String keywords;
    private String sortField = "answer_created_date";
    private String sortOrder = "desc";
}
