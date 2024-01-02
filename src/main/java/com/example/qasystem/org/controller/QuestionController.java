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
        try {
            PageList<Question> pageList = iQuestionService.getQuestionList(questionQuery);
            return new JsonResult().setData(pageList);
        } catch (Exception e) {
            log.error("问题列表出现异常：", e);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("服务器异常");
        }
    }

    // 新增
    @PostMapping("/save")
    public JsonResult addQuestion(@RequestBody Question question){
        try {
            if (question.getId() == null) {
                iQuestionService.insert(question);
                return new JsonResult().setMassage("添加成功！");
            } else {
//                iQuestionService.update(question);
                return new JsonResult().setCode(500).setSuccess(false).setMassage("数据错误！");
            }
        }catch (Exception e){
            log.error("问题保存时出现异常：", e);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("操作失败");
        }
    }

    // 根据id获取详情
    @GetMapping("/{id}")
    public JsonResult selectOne(@PathVariable Long id){
        try {
            Question question=iQuestionService.selectById(id);
            return new  JsonResult().setData(question);
        }catch (Exception e){
            log.error("获取问题详情时出现异常：", e);
            return new JsonResult().setSuccess(false).setCode(500).setMassage("系统繁忙请稍后再试！");
        }
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello 111!";
    }
}
