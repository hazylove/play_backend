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
import com.play.playsystem.post.domain.vo.MainCommentVo;
import com.play.playsystem.post.domain.vo.SubCommentVo;
import com.play.playsystem.post.mapper.CommentMapper;
import com.play.playsystem.post.mapper.PostMapper;
import com.play.playsystem.post.mapper.UserCommentLikesMapper;
import com.play.playsystem.post.service.ICommentService;
import com.play.playsystem.user.domain.vo.UserCreatedVo;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserCommentLikesMapper userCommentLikesMapper;

    @Autowired
    private IUserService userService;
    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public void insert(Comment comment) {
        comment.setCommentCreatedDate(LocalDateTime.now());
        commentMapper.insert(comment);
    }

    @Override
    public PageList<MainCommentVo> getMainCommentList(CommentQuery commentQuery) {
        // 条数
        Long total = commentMapper.count(commentQuery);
        // 分页数据
        List<MainCommentVo> mainCommentVoList = commentMapper.getMainCommentList(commentQuery);
        // 设置创建人
        Map<Long, UserCreatedVo> userCreatedVoMap = new HashMap<>();
        for (MainCommentVo mainCommentVo : mainCommentVoList) {
            Long userId = mainCommentVo.getCommentCreatedId();
            if (!userCreatedVoMap.containsKey(userId)) {
                UserCreatedVo userCreatedVo = userService.getUserCreatedVo(userId);
                userCreatedVoMap.put(userId, userCreatedVo);
            }
            mainCommentVo.setCommentCreatedBy(userCreatedVoMap.get(userId));
        }
        return new PageList<>(total, mainCommentVoList);
    }

    @Override
    public PageList<SubCommentVo> getSubCommentList(CommentQuery commentQuery) {
        // 条数
        Long total = commentMapper.count(commentQuery);
        // 分页数据
        List<SubCommentVo> subCommentVoList = commentMapper.getSubCommentList(commentQuery);

        Map<Long, UserCreatedVo> userCreatedVoMap = new HashMap<>();
        subCommentVoList.forEach(subCommentVo -> {
            Long commentReplyId = subCommentVo.getCommentReplyId();
            Long commentCreatedId = subCommentVo.getCommentCreatedId();
            // 设置回复人
            if (commentReplyId != null) {
                if (!userCreatedVoMap.containsKey(commentReplyId)) {
                    UserCreatedVo userCreatedVo = getCommentCreatedBy(commentReplyId);
                    userCreatedVoMap.put(commentReplyId, userCreatedVo);
                }
                subCommentVo.setCommentReply(userCreatedVoMap.get(commentReplyId));
            }
            // 设置创建人
            if (!userCreatedVoMap.containsKey(commentCreatedId)) {
                UserCreatedVo userCreatedVo = userService.getUserCreatedVo(commentCreatedId);
                userCreatedVoMap.put(commentCreatedId, userCreatedVo);
            }
            subCommentVo.setCommentCreatedBy(userCreatedVoMap.get(commentCreatedId));
        });

        return new PageList<>(total, subCommentVoList);
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
            UserCommentLikes userCommentLikes = new UserCommentLikes(commentId, userId, LocalDateTime.now());
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

    @Override
    public JsonResult deleteComment(Long commentId, Long userId) {
        JsonResult jsonResult = new JsonResult();

        // 帖子发布人
        Long postCreatedId = postMapper.getPostCratedIdByCommentId(commentId);
        // 评论创建人
        Long commentCreatedId = commentMapper.getCommentCreatedIdById(commentId);

        // 只有帖子发布人和评论人可以删除评论
        if (Objects.equals(postCreatedId, userId) || Objects.equals(commentCreatedId, userId)) {
            commentMapper.deleteById(commentId);
            return jsonResult;
        } else {
            return jsonResult.setCode(ResultCode.POST_COMMENT_DELETE_ERROR).setSuccess(false).setMassage("异常删除操作");
        }
    }

    @Override
    public UserCreatedVo getCommentCreatedBy(Long commentId) {
        UserCreatedVo userCreatedVo = commentMapper.getCommentCreatedBy(commentId);
        userCreatedVo.setAvatar(MyFileUtil.reSetFileUrl(userCreatedVo.getAvatar()));
        return userCreatedVo;
    }

}
