package com.play.playsystem.post.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.query.CommentQuery;
import com.play.playsystem.post.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = Long.valueOf(authentication.getName());
            if (comment.getId() == null) {
                comment.setCommentCreatedId(userId);
                iCommentService.insert(comment);
                return new JsonResult().setMassage("添加成功！");
            } else {
//          iCommentService.update(comment);
                return new JsonResult().setCode(ResultCode.ERROR_CODE).setSuccess(false).setMassage("数据错误！");
            }
        } else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

}