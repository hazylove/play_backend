package com.play.playsystem.post.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.service.IPostService;
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
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            postQuery.setUserId(Long.valueOf(authentication.getName()));
        }
        PageList<Post> pageList = postService.getPostList(postQuery);
        return new JsonResult().setData(pageList);
    }

    /**
     * 新增
     * @param post 帖子
     */
    @PostMapping("/save")
    public JsonResult addPost(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Long userId = Long.valueOf(authentication.getName());
            if (post.getId() == null) {
                post.setPostCreatedId(userId);
                postService.insert(post);
                return new JsonResult().setMassage("添加成功！");
            } else {
//          iPostService.update(post);
                return new JsonResult().setCode(ResultCode.ERROR_CODE).setSuccess(false).setMassage("数据错误！");
            }
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    /**
     * 详情
     * @param postId 帖子id
     */
    @GetMapping("/{postId}")
    public JsonResult selectOne(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = Long.valueOf(authentication.getName());
            Post post = postService.selectById(postId, userId);
            return new JsonResult().setData(post);
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }


    @DeleteMapping("/{postId}")
    public JsonResult deletePost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Long userId = Long.valueOf(authentication.getName());
            return postService.deletePost(postId, userId);
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    /**
     * 点赞
     * @param postId 帖子id
     */
    @PostMapping("/like/{postId}")
    public JsonResult likePost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Long userId = Long.valueOf(authentication.getName());
            synchronized (String.valueOf(userId).intern()) {
                return postService.likePost(postId, userId);
            }
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Word!";
    }
}
