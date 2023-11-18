package com.example.qasystem.org.service;

import com.example.qasystem.org.domain.dto.PageList;
import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.QuestionQuery;

public interface IQuestionService {
    PageList<Question> getQuestionList(QuestionQuery questionQuery);

    void insert(Question question);

    Question selectById(Long id);
}
