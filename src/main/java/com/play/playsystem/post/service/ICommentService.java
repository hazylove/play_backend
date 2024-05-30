package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.query.CommentQuery;

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

    boolean commentExists(Long commentId);
}
