package com.example.qasystem.post.controller;

import com.example.qasystem.basic.utils.result.JsonResult;
import com.example.qasystem.basic.utils.dto.PageList;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.post.domain.entity.Post;
import com.example.qasystem.post.domain.query.PostQuery;
import com.example.qasystem.post.service.IPostService;
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
    private IPostService iPostService;

    @PostMapping("/list")
    public JsonResult getPostPage(@RequestBody PostQuery postQuery){
        PageList<Post> pageList = iPostService.getPostList(postQuery);
        return new JsonResult().setData(pageList);
    }

    // 新增
    @PostMapping("/save")
    public JsonResult addPost(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = Long.valueOf(authentication.getName());
            if (post.getId() == null) {
                post.setPostCreatedId(userId);
                iPostService.insert(post);
                return new JsonResult().setMassage("添加成功！");
            } else {
//          iPostService.update(post);
                return new JsonResult().setCode(ResultCode.ERROR_CODE).setSuccess(false).setMassage("数据错误！");
            }
        }else {
            return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMassage("未认证用户！");
        }
    }

    // 根据id获取详情
    @GetMapping("/{id}")
    public JsonResult selectOne(@PathVariable Long id){
        Post post = iPostService.selectById(id);
        return new  JsonResult().setData(post);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Word!";
    }
}
