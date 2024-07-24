package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.*;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;
import com.play.playsystem.post.mapper.*;
import com.play.playsystem.post.service.IFavoriteService;
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

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    private IFavoriteService favoriteService;

    // 点赞、拉黑锁
    private static final ConcurrentHashMap<Long, Lock> likeBlockLocks = new ConcurrentHashMap<>();

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
        if (postId == null || postNotExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMessage("帖子不存在");
        }

        Lock lock = likeBlockLocks.computeIfAbsent(postId, k -> new ReentrantLock());

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
                        QueryWrapper<UserPostBlock> blockQueryWrapper = new QueryWrapper<>();
                        blockQueryWrapper.lambda().eq(UserPostBlock::getPostId, postId).eq(UserPostBlock::getUserId, userId);
                        if (userPostBlockMapper.delete(blockQueryWrapper) > 0) {
                            // 更新拉黑总数
                            if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostBlocksNum, 0).setSql("post_blocks_num = post_blocks_num - 1").update()) {
                                return jsonResult;
                            }
                            throw new RuntimeException("帖子点赞操作数据异常");
                        }
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
            synchronized (likeBlockLocks) {
                if (((ReentrantLock) lock).getQueueLength() == 0) {
                    likeBlockLocks.remove(postId);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult blockPost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (postId == null || postNotExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMessage("帖子不存在");
        }

        Long createdId = postMapper.getCreatedIdById(postId);
        if (Objects.equals(createdId, userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("拉黑操作异常");
        }

        Lock lock = likeBlockLocks.computeIfAbsent(postId, k -> new ReentrantLock());

        // 加锁
        lock.lock();
        try {
            // 查询是否已拉黑
            QueryWrapper<UserPostBlock> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserPostBlock::getPostId, postId).eq(UserPostBlock::getUserId, userId);
            if (userPostBlockMapper.selectOne(queryWrapper) == null) {
                // 未拉黑 新增拉黑数据
                UserPostBlock userPostBlock = new UserPostBlock(postId, userId, LocalDateTime.now());
                if (userPostBlockMapper.insert(userPostBlock) > 0) {
                    // 更新帖子拉黑数
                    if (lambdaUpdate().eq(Post::getId, postId).setSql("post_blocks_num = post_blocks_num + 1").update()) {
                        // 如果已点赞、同时取消点赞状态
                        QueryWrapper<UserPostLike> likeQueryWrapper = new QueryWrapper<>();
                        likeQueryWrapper.lambda().eq(UserPostLike::getPostId, postId).eq(UserPostLike::getUserId, userId);
                        if (userPostLikeMapper.delete(likeQueryWrapper) > 0) {
                            // 更新点赞总数
                            if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostLikesNum, 0).setSql("post_likes_num = post_likes_num - 1").update()) {
                                return jsonResult;
                            }
                            throw new RuntimeException("帖子拉黑操作数据异常");
                        }
                        return jsonResult;
                    }
                    throw new RuntimeException("帖子拉黑操作数据异常");
                }
            } else {
                // 已拉黑 删除拉黑数据
                if (userPostBlockMapper.delete(queryWrapper) > 0) {
                    // 更新帖子拉黑数
                    if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostBlocksNum, 0).setSql("post_blocks_num = post_blocks_num - 1").update()) {
                        return jsonResult;
                    }
                }
                throw new RuntimeException("帖子取消拉黑操作数据异常");
            }
        } finally {
            lock.unlock();
            synchronized (likeBlockLocks) {
                if (((ReentrantLock) lock).getQueueLength() == 0) {
                    likeBlockLocks.remove(postId);
                }
            }
        }
        return null;
    }

    @Override
    public boolean postNotExists(Long postId) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getId, postId);
        return postMapper.selectOne(queryWrapper) == null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deletePost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();

        // 当前只有帖子发布人可以删除帖子
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getId, postId).eq(Post::getPostCreatedId, userId);
        if (postMapper.delete(queryWrapper) > 0) {
            return jsonResult;
        } else {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("异常删除操作");
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

    @Override
    public PageList<PostVo> getBlockPostList(PostQuery postQuery) {
        // 条数
        Long total = postMapper.countBlockPost(postQuery);
        // 分页数据
        List<PostVo> postVoList = postMapper.getBlockPostList(postQuery);
        // 设置创建人
        return setPostVoCreator(total, postVoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult collectPost(Long postId, Long favoriteId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (postId == null || postNotExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMessage("帖子不存在");
        }

        Long favoriteCreatedId = favoriteMapper.getCreatedIdById(favoriteId);
        if (!Objects.equals(favoriteCreatedId, userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("收藏操作异常");
        }

        // 查询是否已收藏
        QueryWrapper<UserPostFavorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().eq(UserPostFavorite::getPostId, postId).eq(UserPostFavorite::getFavoriteId, favoriteId);
        if (userPostFavoriteMapper.selectOne(favoriteQueryWrapper) == null) {
            // 未收藏
            UserPostFavorite userPostFavorite = new UserPostFavorite(postId, userId, favoriteId, LocalDateTime.now());
            if (userPostFavoriteMapper.insert(userPostFavorite) > 0) {
                return jsonResult;
            }
            throw new RuntimeException("帖子收藏操作数据异常");
        } else {
            // 已收藏
            if (userPostFavoriteMapper.delete(favoriteQueryWrapper) > 0) {
                return jsonResult;
            }
            throw new RuntimeException("帖子取消收藏操作数据异常");
        }
    }

    @Override
    public JsonResult getCollectPostPageList(PostQuery postQuery) {
        JsonResult jsonResult = new JsonResult();
        // 参数校验
        if (postQuery.getFavoriteId() == null) {
            return jsonResult.setCode(ResultCode.UNPROCESSABLE_ENTITY).setSuccess(false).setMessage("缺少必要的参数：favoriteId");
        }
        // 操作权限校验
        if (!favoriteService.checkFavorite(postQuery.getFavoriteId(), postQuery.getUserId())) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作");
        }
        // 条数
        Long total = postMapper.countCollectPost(postQuery);
        // 分页数据
        List<PostVo> postVoList = postMapper.getCollectPostList(postQuery);
        return jsonResult.setData(setPostVoCreator(total, postVoList));
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
}

