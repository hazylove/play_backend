package com.example.qasystem.post.mapper;

import com.example.qasystem.post.domain.entity.Answer;
import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.AnswerQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper {
    Long count(AnswerQuery answerQuery);

    List<Question> getList(AnswerQuery answerQuery);

    void insert(Answer answer);
}
