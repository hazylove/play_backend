package com.example.qasystem.org.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.org.domain.dto.PageList;
import com.example.qasystem.org.domain.entity.Question;
import com.example.qasystem.org.domain.query.QuestionQuery;
import com.example.qasystem.org.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    @PostMapping("/list")
    public JsonResult getQuestionPage(@RequestBody QuestionQuery questionQuery){
        PageList<Question> pageList = iQuestionService.getQuestionList(questionQuery);
        return new JsonResult().setData(pageList);
    }

    // 新增
    @PostMapping("/save")
    public JsonResult addQuestion(@RequestBody Question question){
        if (question.getId() == null) {
            iQuestionService.insert(question);
            return new JsonResult().setMassage("添加成功！");
        } else {
//          iQuestionService.update(question);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("数据错误！");
        }

    }

    // 根据id获取详情
    @GetMapping("/{id}")
    public JsonResult selectOne(@PathVariable Long id){
        Question question=iQuestionService.selectById(id);
        return new  JsonResult().setData(question);
    }

    @GetMapping("/hello")
    public String hello() {
//        throw new Exception("服务器异常");
        return "Hello Word!";
    }
}
