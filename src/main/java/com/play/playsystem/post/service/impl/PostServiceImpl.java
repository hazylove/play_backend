package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.entity.UserPostLikes;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.mapper.PostMapper;
import com.play.playsystem.post.mapper.UserPostLikesMapper;
import com.play.playsystem.post.service.IPostService;
import com.play.playsystem.user.domain.entity.User;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostLikesMapper userPostLikesMapper;

    @Autowired
    private IUserService userService;

    @Override
    public PageList<Post> getPostList(PostQuery postQuery) {
        // 条数
        Long total = postMapper.count(postQuery);
        // 分页数据
        List<Post> posts = postMapper.getPostList(postQuery);
        // 设置创建人
        Map<Long, User> userMap = new HashMap<>();
        for (Post post : posts) {
            Long userId = post.getPostCreatedId();
            if (!userMap.containsKey(post.getPostCreatedId())) {
                User user = userService.getUserInfo(userId);
                // 重置用户头像
                user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar()));
                userMap.put(userId, user);
            }
            post.setPostCreatedBy(userMap.get(userId));
        }
        return new PageList<>(total, posts);
    }

    @Override
    @Transactional
    public void insert(Post post) {
        post.setPostCreatedDate(Calendar.getInstance().getTime());
        postMapper.insert(post);
    }

    @Override
    public Post selectById(Long id, Long userId) {
        Post post = postMapper.selectById(id, userId);
        User user = userService.getUserInfo(post.getPostCreatedId());
        post.setPostCreatedBy(user);
        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult likePost(Long postId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (postId == null || !postExists(postId)) {
            return jsonResult.setCode(ResultCode.POST_NOT_EXIST).setSuccess(false).setMassage("帖子不存在");
        }
        // 查询是否已点赞
        QueryWrapper<UserPostLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserPostLikes::getPostId, postId).eq(UserPostLikes::getUserId, userId);
        if (userPostLikesMapper.selectOne(queryWrapper) == null) {
            // 未点赞
            UserPostLikes userPostLikes = new UserPostLikes(postId, userId);
            if (userPostLikesMapper.insert(userPostLikes) > 0) {
                // 更新帖子点赞数
                if (lambdaUpdate().eq(Post::getId, postId).setSql("post_likes_num = post_likes_num + 1").update()) {
                    return jsonResult;
                }
            }
            throw new RuntimeException("点赞操作数据异常");
        } else {
            // 已点赞
            if (userPostLikesMapper.delete(queryWrapper) > 0){
                // 更新帖子点赞数
                if (lambdaUpdate().eq(Post::getId, postId).gt(Post::getPostLikesNum, 0).setSql("post_likes_num = post_likes_num - 1").update()) {
                    return jsonResult;
                }
            }
            throw new RuntimeException("取消点赞操作数据异常");
        }
    }

    @Override
    public boolean postExists(Long postId) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getId, postId);
        return postMapper.selectOne(queryWrapper) != null;
    }
}




