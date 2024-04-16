package com.example.qasystem.post.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Answer;
import com.example.qasystem.post.domain.entity.Question;
import com.example.qasystem.file.domain.query.AnswerQuery;
import com.example.qasystem.post.service.IAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/answer")
@Slf4j
public class AnswerController {

    @Autowired
    private IAnswerService iAnswerService;


    @PostMapping("/list")
    public JsonResult getAnswerPage(@RequestBody AnswerQuery answerQuery){
        PageList<Question> pageList = iAnswerService.getList(answerQuery);
        return new JsonResult().setData(pageList);
    }

    // 新增
    @PostMapping("/save")
    public JsonResult addQuestion(@RequestBody Answer answer){
        if (answer.getId() == null) {
            iAnswerService.insert(answer);
            return new JsonResult().setMassage("添加成功！");
        } else {
//          iAnswerService.update(answer);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("数据错误！");
        }
    }


}
