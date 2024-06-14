package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.post.domain.entity.Comment;
import com.play.playsystem.post.domain.entity.UserCommentBlock;
import com.play.playsystem.post.domain.entity.UserCommentLike;
import com.play.playsystem.post.domain.query.CommentQuery;
import com.play.playsystem.post.domain.vo.MainCommentVo;
import com.play.playsystem.post.domain.vo.SubCommentVo;
import com.play.playsystem.post.mapper.CommentMapper;
import com.play.playsystem.post.mapper.PostMapper;
import com.play.playsystem.post.mapper.UserCommentBlockMapper;
import com.play.playsystem.post.mapper.UserCommentLikeMapper;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserCommentLikeMapper userCommentLikeMapper;

    @Autowired
    private UserCommentBlockMapper userCommentBlockMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private PostMapper postMapper;

    private static final ConcurrentHashMap<String, Lock> likeBlockLocks = new ConcurrentHashMap<>();

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

        String key = generateKey(userId, commentId);
        Lock lock = likeBlockLocks.computeIfAbsent(key, k -> new ReentrantLock());

        // 锁定
        lock.lock();
        try {
            // 查询是否已点赞
            QueryWrapper<UserCommentLike> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.lambda().eq(UserCommentLike::getCommentId, commentId).eq(UserCommentLike::getUserId, userId);
            if (userCommentLikeMapper.selectOne(likeQueryWrapper) == null) {
                // 未点赞
                UserCommentLike userCommentLike = new UserCommentLike(commentId, userId, LocalDateTime.now());
                if (userCommentLikeMapper.insert(userCommentLike) > 0) {
                    // 更新评论点赞数
                    if (lambdaUpdate().eq(Comment::getId, commentId).setSql("comment_likes_num = comment_likes_num + 1").update()) {
                        // 查询是否已拉黑
                        QueryWrapper<UserCommentBlock> blockQueryWrapper = new QueryWrapper<>();
                        blockQueryWrapper.lambda().eq(UserCommentBlock::getCommentId, commentId).eq(UserCommentBlock::getUserId, userId);
                        if (userCommentBlockMapper.delete(blockQueryWrapper) > 0) {
                            // 更新评论拉黑
                            if (lambdaUpdate().eq(Comment::getId, commentId).gt(Comment::getCommentLikesNum, 0).setSql("comment_blocks_num = comment_blocks_num - 1").update()) {
                                return jsonResult;
                            }
                            throw new RuntimeException("评论点赞操作数据异常");
                        }
                        return jsonResult;
                    }
                }
                throw new RuntimeException("评论点赞操作数据异常");
            } else {
                // 已点赞
                if (userCommentLikeMapper.delete(likeQueryWrapper) > 0) {
                    // 更新评论点赞数
                    if (lambdaUpdate().eq(Comment::getId, commentId).gt(Comment::getCommentLikesNum, 0).setSql("comment_likes_num = comment_likes_num - 1").update()) {
                        return jsonResult;
                    }
                }
                throw new RuntimeException("评论取消点赞操作数据异常");
            }
        } finally {
            // 解锁
            lock.unlock();
            // 移除锁对象
            likeBlockLocks.remove(key);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult blockComment(Long commentId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (commentId == null || !commentExists(commentId)) {
            return jsonResult.setCode(ResultCode.COMMENT_NOT_EXIST).setSuccess(false).setMassage("评论不存在");
        }

        String key = generateKey(userId, commentId);
        Lock lock = likeBlockLocks.computeIfAbsent(key, k -> new ReentrantLock());

        // 锁定
        lock.lock();
        try {
            // 查询是否已拉黑
            QueryWrapper<UserCommentBlock> blockQueryWrapper = new QueryWrapper<>();
            blockQueryWrapper.lambda().eq(UserCommentBlock::getCommentId, commentId).eq(UserCommentBlock::getUserId, userId);
            if (userCommentBlockMapper.selectOne(blockQueryWrapper) == null) {
                // 未拉黑
                UserCommentBlock block = new UserCommentBlock(commentId, userId, LocalDateTime.now());
                if (userCommentBlockMapper.insert(block) > 0) {
                    // 更新评论拉黑数
                    if (lambdaUpdate().eq(Comment::getId, commentId).setSql("comment_blocks_num = comment_blocks_num + 1").update()) {
                        QueryWrapper<UserCommentLike> likeQueryWrapper = new QueryWrapper<>();
                        likeQueryWrapper.lambda().eq(UserCommentLike::getCommentId, commentId).eq(UserCommentLike::getUserId, userId);
                        // 删除原有点赞
                        if (userCommentLikeMapper.delete(likeQueryWrapper) > 0) {
                            // 更新评论点赞数
                            if (lambdaUpdate().eq(Comment::getId, commentId).gt(Comment::getCommentLikesNum, 0).setSql("comment_likes_num = comment_likes_num - 1").update()) {
                                return jsonResult;
                            }
                            throw new RuntimeException("评论拉黑操作数据异常");
                        }
                        return jsonResult;
                    }
                    throw new RuntimeException("评论拉黑操作数据异常");
                } else {
                    // 已拉黑
                    if (userCommentBlockMapper.delete(blockQueryWrapper) > 0) {
                        // 更新评论拉黑
                        if (lambdaUpdate().eq(Comment::getId, commentId).gt(Comment::getCommentLikesNum, 0).setSql("comment_blocks_num = comment_blocks_num - 1").update()) {
                            return jsonResult;
                        }
                    }
                    throw new RuntimeException("评论取消点赞操作数据异常");
                }
            }
        } finally {
            lock.unlock();
            likeBlockLocks.remove(key);
        }
        return null;
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
        Long postCreatedId = postMapper.getCratedIdByCommentId(commentId);
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

    // 生成复合键
    private static String generateKey(Long userId, Long commentId) {
        return userId + "_" + commentId;
    }
}
