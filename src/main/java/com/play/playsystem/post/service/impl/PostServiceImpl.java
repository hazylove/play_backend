package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.entity.UserPostBlock;
import com.play.playsystem.post.domain.entity.UserPostLike;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;
import com.play.playsystem.post.mapper.PostMapper;
import com.play.playsystem.post.mapper.UserPostBlockMapper;
import com.play.playsystem.post.mapper.UserPostLikeMapper;
import com.play.playsystem.post.service.IPostService;
import com.play.playsystem.user.domain.vo.UserCreatedVo;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostLikeMapper userPostLikeMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserPostBlockMapper userPostBlockMapper;

    private static final ConcurrentHashMap<String, Lock> likeLocks = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Lock> blockLocks = new ConcurrentHashMap<>();

    @Override
    public PageList<PostVo> getPostList(PostQuery postQuery) {
        // 条数
        Long total = postMapper.count(postQuery);
        // 分页数据
        List<PostVo> postVoList = postMapper.getPostList(postQuery);
        // 设置创建人
        return setPostVoCreator(total, postVoList);
    }

    @Override
    @Transactional
    public void insert(Post post) {
        post.setPostCreatedDate(LocalDateTime.now());
        postMapper.insert(post);
    }

    @Override
    public PostVo selectById(Long id, Long userId) {
        PostVo postVo = postMapper.selectById(id, userId);
        // 设置创建人
        UserCreatedVo userCreatedVo = userService.getUserCreatedVo(postVo.getPostCreatedId());
        postVo.setPostCreatedBy(userCreatedVo);
        return postVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult likePost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (postId == null || !postExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMassage("帖子不存在");
        }

        String key = generateKey(userId, postId);
        Lock lock = likeLocks.computeIfAbsent(key, k -> new ReentrantLock());

        // 锁定
        lock.lock();
        try {
            // 查询是否已点赞
            QueryWrapper<UserPostLike> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserPostLike::getPostId, postId).eq(UserPostLike::getUserId, userId);
            if (userPostLikeMapper.selectOne(queryWrapper) == null) {
                // 未点赞
                UserPostLike userPostLike = new UserPostLike(postId, userId, LocalDateTime.now());
                if (userPostLikeMapper.insert(userPostLike) > 0) {
                    // 更新帖子点赞数
                    if (lambdaUpdate().eq(Post::getId, postId).setSql("post_likes_num = post_likes_num + 1").update()) {
                        return jsonResult;
                    }
                }
                throw new RuntimeException("帖子点赞操作数据异常");
            } else {
                // 已点赞
                if (userPostLikeMapper.delete(queryWrapper) > 0) {
                    // 更新帖子点赞数
                    if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostLikesNum, 0).setSql("post_likes_num = post_likes_num - 1").update()) {
                        return jsonResult;
                    }
                }
                throw new RuntimeException("帖子取消点赞操作数据异常");
            }
        } finally {
            // 解锁
            lock.unlock();
            likeLocks.remove(key);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult blockPost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (postId == null || !postExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMassage("帖子不存在");
        }

        Long createdId = postMapper.getCreatedIdByPostId(postId);
        if (Objects.equals(createdId, userId)) {
            return jsonResult.setCode(ResultCode.POST_COMMENT_BLOCK_ERROR).setSuccess(false).setMassage("拉黑操作异常");
        }

        String key = generateKey(userId, postId);
        Lock lock = blockLocks.computeIfAbsent(key, k -> new ReentrantLock());

        // 加锁
        lock.lock();
        try {
            // 查询是否已拉黑
            QueryWrapper<UserPostBlock> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserPostBlock::getPostId, postId).eq(UserPostBlock::getUserId, userId);
            if (userPostBlockMapper.selectOne(queryWrapper) == null) {
                // 未拉黑 新增拉黑数据
                UserPostBlock userPostBlock = new UserPostBlock(userId, postId, LocalDateTime.now());
                if (userPostBlockMapper.insert(userPostBlock) > 0) {
                    // 更新帖子拉黑数
                    if (lambdaUpdate().eq(Post::getId, postId).setSql("post_blocks_num = post_blocks_num + 1").update()) {
                        return jsonResult;
                    }
                    throw new RuntimeException("帖子拉黑操作数据异常");
                }
            } else {
                // 已拉黑 删除拉黑数据
                if (userPostBlockMapper.delete(queryWrapper) > 0) {
                    // 更新帖子拉黑数
                    if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostLikesNum, 0).setSql("post_blocks_num = post_blocks_num - 1").update()) {
                        return jsonResult;
                    }
                }
                throw new RuntimeException("帖子取消拉黑操作数据异常");
            }
        } finally {
            lock.unlock();
            blockLocks.remove(key);
        }
        return null;
    }

    @Override
    public boolean postExists(Long postId) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getId, postId);
        return postMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public JsonResult deletePost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();

        // 当前只有帖子发布人可以删除帖子
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getId, postId).eq(Post::getPostCreatedId, userId);
        if (postMapper.delete(queryWrapper) > 0) {
            return jsonResult;
        } else {
            return jsonResult.setCode(ResultCode.POST_COMMENT_DELETE_ERROR).setSuccess(false).setMassage("异常删除操作");
        }
    }

    @Override
    public PageList<PostVo> getLikePostList(PostQuery postQuery) {
        // 条数
        Long total = postMapper.countLikePost(postQuery);
        // 分页数据
        List<PostVo> postVoList = postMapper.getLikePostList(postQuery);
        // 设置创建人
        return setPostVoCreator(total, postVoList);
    }

    private PageList<PostVo> setPostVoCreator(Long total, List<PostVo> postVoList) {
        Map<Long, UserCreatedVo> userCreatedVoMap = new HashMap<>();
        postVoList.forEach(postVo -> {
            Long userId = postVo.getPostCreatedId();
            if (!userCreatedVoMap.containsKey(userId)) {
                UserCreatedVo userCreatedVo = userService.getUserCreatedVo(userId);
                userCreatedVoMap.put(userId, userCreatedVo);
            }
            postVo.setPostCreatedBy(userCreatedVoMap.get(userId));
        });
        return new PageList<>(total, postVoList);
    }

    // 生成复合键
    private static String generateKey(Long userId, Long commentId) {
        return userId + "_" + commentId;
    }
}




