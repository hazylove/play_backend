package com.example.qasystem.org.controller;

import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.QuestionQuery;
import com.example.qasystem.org.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    @PostMapping("/list")
    public Question getQuestionPage(@RequestBody QuestionQuery questionQuery){

        return null;
    }
}
