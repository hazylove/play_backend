package com.example.qasystem.post.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.post.domain.entity.Comment;
import com.example.qasystem.post.domain.query.CommentQuery;
import com.example.qasystem.post.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/comment")
@Slf4j
public class CommentController {

    @Autowired
    private ICommentService iCommentService;


    /**
     * 获取主评论分页列表
     * @param commentQuery 查询参数
     * @return 数据列表
     */
    @PostMapping("/mainList")
    public JsonResult getMainCommentList(@RequestBody CommentQuery commentQuery){
        PageList<Comment> pageList = iCommentService.getMainCommentList(commentQuery);
        return new JsonResult().setData(pageList);
    }

    @PostMapping("/subList")
    public JsonResult getSubCommentList(@RequestBody CommentQuery commentQuery){
        PageList<Comment> pageList = iCommentService.getSubCommentList(commentQuery);
        return new JsonResult().setData(pageList);
    }

    /**
     * 新建评论
     * @param comment 评论
     * @return 操作结果
     */
    @PostMapping("/save")
    public JsonResult addQuestion(@RequestBody Comment comment){
        if (comment.getId() == null) {
            iCommentService.insert(comment);
            return new JsonResult().setMassage("添加成功！");
        } else {
//          iCommentService.update(comment);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("数据错误！");
        }
    }


}
