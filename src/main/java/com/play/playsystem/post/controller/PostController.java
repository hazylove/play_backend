package com.play.playsystem.post.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;
import com.play.playsystem.post.service.IPostService;
import com.play.playsystem.user.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/post")
@Slf4j
public class PostController {

    @Autowired
    private IPostService postService;

    /**
     * 列表
     * @param postQuery 查询参数
     */
    @PostMapping("/list")
    public JsonResult getPostPage(@RequestBody PostQuery postQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            postQuery.setUserId(Long.valueOf(authentication.getName()));
        }
        PageList<PostVo> pageList = postService.getPostList(postQuery);
        return new JsonResult().setData(pageList);
    }

    /**
     * 新增
     * @param post 帖子
     */
    @PostMapping("/add")
    public JsonResult addPost(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            post.setPostCreatedId(userId);
            postService.insert(post);
            return new JsonResult();
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 详情
     * @param postId 帖子id
     */
    @GetMapping("/{postId}")
    public JsonResult selectOne(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            PostVo postVo = postService.selectById(postId, userId);
            return new JsonResult().setData(postVo);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 删除帖子
     * @param postId 帖子id
     */
    @DeleteMapping("/{postId}")
    public JsonResult deletePost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return postService.deletePost(postId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 点赞
     * @param postId 帖子id
     */
    @PostMapping("/like/{postId}")
    public JsonResult likePost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return postService.likePost(postId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 获取点赞列表
     * @param postQuery 查询参数
     */
    @PostMapping("/likeList")
    public JsonResult getLikePostPage(@RequestBody PostQuery postQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            postQuery.setUserId(Long.valueOf(authentication.getName()));
            PageList<PostVo> pageList = postService.getLikePostList(postQuery);
            return new JsonResult().setData(pageList);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 拉黑帖子
     * @param postId 帖子id
     */
    @PostMapping("/block/{postId}")
    public JsonResult blockPost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return postService.blockPost(postId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 拉黑列表
     * @param postQuery 查询参数
     */
    @PostMapping("/blockList")
    public JsonResult getBlockPostPage(@RequestBody PostQuery postQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            postQuery.setUserId(Long.valueOf(authentication.getName()));
            PageList<PostVo> pageList = postService.getBlockPostList(postQuery);
            return new JsonResult().setData(pageList);
        } else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
        }
    }

    /**
     * 收藏
     * @param postId 帖子id
     * @param favoriteId 收藏夹id
     */
    @PostMapping("/collect")
    public JsonResult collectPost(@RequestParam Long postId, @RequestParam Long favoriteId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return postService.collectPost(postId, favoriteId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 收藏列表
     * @param postQuery 查询参数
     */
    @PostMapping("/collectList")
    public JsonResult getCollectPostPage(@RequestBody PostQuery postQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.checkAuth(authentication)) {
            postQuery.setUserId(Long.valueOf(authentication.getName()));
            return postService.getCollectPostPageList(postQuery);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Word!";
    }
}
