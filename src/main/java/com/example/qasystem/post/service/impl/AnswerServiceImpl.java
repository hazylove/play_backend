package com.example.qasystem.post.service.impl;

import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Answer;
import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.AnswerQuery;
import com.example.qasystem.post.mapper.AnswerMapper;
import com.example.qasystem.post.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class AnswerServiceImpl implements IAnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public PageList<Question> getList(AnswerQuery answerQuery) {
        //条数
        Long total =answerMapper.count(answerQuery);
        //分页数据
        List<Question> questions = answerMapper.getList(answerQuery);
        return new PageList<>(total,questions);

    }

    @Override
    @Transactional
    public void insert(Answer answer) {
        answer.setAnswerCreatedDate(Calendar.getInstance().getTime());
        answerMapper.insert(answer);
    }


}
