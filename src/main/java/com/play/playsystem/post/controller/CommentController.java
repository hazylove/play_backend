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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/comment")
@Slf4j
public class CommentController {

    @Autowired
    private ICommentService commentService;


    /**
     * 获取主评论分页列表
     * @param commentQuery 查询参数
     * @return 数据
     */
    @PostMapping("/mainList")
    public JsonResult getMainCommentList(@RequestBody CommentQuery commentQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            commentQuery.setUserId(Long.valueOf(authentication.getName()));
            PageList<Comment> pageList = commentService.getMainCommentList(commentQuery);
            return new JsonResult().setData(pageList);
        } else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    /**
     * 子评论分页列表
     * @param commentQuery 查询参数
     * @return 数据
     */
    @PostMapping("/subList")
    public JsonResult getSubCommentList(@RequestBody CommentQuery commentQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            commentQuery.setUserId(Long.valueOf(authentication.getName()));
            PageList<Comment> pageList = commentService.getSubCommentList(commentQuery);
            return new JsonResult().setData(pageList);
        } else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    /**
     * 新建评论
     * @param comment 评论
     */
    @PostMapping("/save")
    public JsonResult addComment(@RequestBody Comment comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Long userId = Long.valueOf(authentication.getName());
            if (comment.getId() == null) {
                comment.setCommentCreatedId(userId);
                commentService.insert(comment);
                return new JsonResult().setMassage("添加成功！");
            } else {
//          iCommentService.update(comment);
                return new JsonResult().setCode(ResultCode.ERROR_CODE).setSuccess(false).setMassage("数据错误！");
            }
        } else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    @PostMapping("/like/{commentId}")
    public JsonResult likePost(@PathVariable Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Long userId = Long.valueOf(authentication.getName());
            synchronized (String.valueOf(userId).intern()) {
                return commentService.likeComment(commentId, userId);
            }
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }
}
