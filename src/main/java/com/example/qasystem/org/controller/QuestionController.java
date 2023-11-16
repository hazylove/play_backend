package com.example.qasystem.org.controller;

import com.example.qasystem.basic.utils.JsonResult;
import com.example.qasystem.org.domain.dto.PageList;
import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.QuestionQuery;
import com.example.qasystem.org.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    @PostMapping("/list")
    public JsonResult getQuestionPage(@RequestBody QuestionQuery questionQuery){
        PageList<Question> pageList = iQuestionService.getQuestionList(questionQuery);
        return new JsonResult().setData(pageList);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello 111!";
    }
}
