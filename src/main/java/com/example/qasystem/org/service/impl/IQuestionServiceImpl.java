package com.example.qasystem.org.service.impl;

import com.example.qasystem.org.domain.dto.PageList;
import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.QuestionQuery;
import com.example.qasystem.org.mapper.QuestionMapper;
import com.example.qasystem.org.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class IQuestionServiceImpl implements IQuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public PageList<Question> getQuestionList(QuestionQuery questionQuery) {
        //条数
        Long total =questionMapper.count(questionQuery);
        //分页数据
        List<Question> questions = questionMapper.getQuestionList(questionQuery);
        return new PageList<>(total,questions);
    }

    @Override
    @Transactional
    public void insert(Question question) {
        question.setQuestionCreatedDate(Calendar.getInstance().getTime());
        questionMapper.insert(question);
    }

    @Override
    public Question selectById(Long id) {
        return questionMapper.selectById(id);
    }
}
