package com.example.qasystem.post.service;

import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.QuestionQuery;

public interface IQuestionService {
    PageList<Question> getQuestionList(QuestionQuery questionQuery);

    void insert(Question question);

    Question selectById(Long id);
}
