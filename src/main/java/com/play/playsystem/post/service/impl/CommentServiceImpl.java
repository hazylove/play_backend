package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.entity.UserCommentLikes;
import com.play.playsystem.post.domain.query.CommentQuery;
import com.play.playsystem.post.mapper.CommentMapper;
import com.play.playsystem.post.mapper.UserCommentLikesMapper;
import com.play.playsystem.post.service.ICommentService;
import com.play.playsystem.user.domain.entity.User;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserCommentLikesMapper userCommentLikesMapper;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void insert(Comment comment) {
        comment.setCommentCreatedDate(LocalDateTime.now());
        commentMapper.insert(comment);
    }

    @Override
    public PageList<Comment> getMainCommentList(CommentQuery commentQuery) {
        // 条数
        Long total = commentMapper.count(commentQuery);
        // 分页数据
        List<Comment> comments = commentMapper.getMainCommentList(commentQuery);
        // 设置创建人
        return reSetCommentCreatedUserAvatar(total, comments);
    }

    @Override
    public PageList<Comment> getSubCommentList(CommentQuery commentQuery) {
        //条数
        Long total = commentMapper.count(commentQuery);
        //分页数据
        List<Comment> comments = commentMapper.getSubCommentList(commentQuery);
        // 设置创建人
        return reSetCommentCreatedUserAvatar(total, comments);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult likeComment(Long commentId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (commentId == null || !commentExists(commentId)) {
            return jsonResult.setCode(ResultCode.COMMENT_NOT_EXIST).setSuccess(false).setMassage("评论不存在");
        }
        // 查询是否已点赞
        QueryWrapper<UserCommentLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCommentLikes::getCommentId, commentId).eq(UserCommentLikes::getUserId, userId);
        if (userCommentLikesMapper.selectOne(queryWrapper) == null) {
            // 未点赞
            UserCommentLikes userCommentLikes = new UserCommentLikes(commentId, userId);
            if (userCommentLikesMapper.insert(userCommentLikes) > 0) {
                // 更新评论点赞数
                if (lambdaUpdate().eq(Comment::getId, commentId).setSql("comment_likes_num = comment_likes_num + 1").update()) {
                    return jsonResult;
                }
            }
            throw new RuntimeException("评论点赞操作数据异常");
        } else {
            // 已点赞
            if (userCommentLikesMapper.delete(queryWrapper) > 0) {
                // 更新评论点赞数
                if (lambdaUpdate().eq(Comment::getId, commentId).gt(Comment::getCommentLikesNum, 0).setSql("comment_likes_num = comment_likes_num - 1").update()) {
                    return jsonResult;
                }
            }
            throw new RuntimeException("评论取消点赞操作数据异常");
        }
    }

    @Override
    public boolean commentExists(Long commentId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getId, commentId);
        return commentMapper.selectOne(queryWrapper) != null;
    }

    private PageList<Comment> reSetCommentCreatedUserAvatar(Long total, List<Comment> comments) {
        Map<Long, User> userMap = new HashMap<>();
        for (Comment comment : comments) {
            Long userId = comment.getCommentCreatedId();
            if (!userMap.containsKey(userId)) {
                User user = userService.getUserInfo(userId);
                // 设置用户头像
                user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar()));
                userMap.put(userId, user);
            }
            comment.setCommentCreatedBy(userMap.get(userId));
        }
        return new PageList<>(total, comments);
    }

}
