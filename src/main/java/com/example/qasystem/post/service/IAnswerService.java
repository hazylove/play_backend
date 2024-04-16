package com.example.qasystem.post.service;

import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Answer;
import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.AnswerQuery;

public interface IAnswerService {
    PageList<Question> getList(AnswerQuery answerQuery);

    void insert(Answer answer);
}
