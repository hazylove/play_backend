package com.example.qasystem.file.domain.query;

import com.example.qasystem.basic.utils.dto.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQuery extends PageQuery {
    private String keywords;
    private String sortField = "question_created_date";
    private String sortOrder = "desc";
}
