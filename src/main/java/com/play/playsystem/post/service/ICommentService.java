package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.query.CommentQuery;
import com.play.playsystem.user.domain.entity.User;

public interface ICommentService extends IService<Comment> {
    /**
     * 主评论分页列表
     * @param commentQuery 查询参数
     * @return 数据
     */
    PageList<Comment> getMainCommentList(CommentQuery commentQuery);

    /**
     * 新建评论
     * @param comment 评论
     */
    void insert(Comment comment);

    /**
     * 子评论分页列表
     * @param commentQuery 查询参数
     * @return 数据
     */
    PageList<Comment> getSubCommentList(CommentQuery commentQuery);

    /**
     * 点赞/取消点赞评论
     * @param commentId 评论id
     * @param userId 用户id
     */
    JsonResult likeComment(Long commentId, Long userId);

    /**
     * 判断评论是否存在
     * @param commentId 评论id
     * @return 是否存在
     */
    boolean commentExists(Long commentId);

    /**
     * 根据评论id、创建人id删除评论
     * @param commentId 评论id
     * @param userId 创建人id
     */
    JsonResult deleteComment(Long commentId, Long userId);

    /**
     * 根据评论id获取评论创建人信息
     * @param commentId 评论id
     * @return 创建人
     */
    User getCommentCreatedBy(Long commentId);
}
