package com.example.qasystem.post.mapper;


import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.QuestionQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    Long count(QuestionQuery questionQuery);

    List<Question> getQuestionList(QuestionQuery questionQuery);

    void insert(Question question);

    Question selectById(Long id);
}
