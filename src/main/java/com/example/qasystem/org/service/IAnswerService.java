package com.example.qasystem.org.service;

import com.example.qasystem.org.domain.dto.PageList;
import com.example.qasystem.org.domain.entity.Answer;
import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.AnswerQuery;

public interface IAnswerService {
    PageList<Question> getList(AnswerQuery answerQuery);

    void insert(Answer answer);
}
