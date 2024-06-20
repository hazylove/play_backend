package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.post.domain.entity.Post;
import com.play.playsystem.post.domain.query.PostQuery;
import com.play.playsystem.post.domain.vo.PostVo;

public interface IPostService extends IService<Post> {
    /**
     * 分页列表
     * @param postQuery 查询参数
     * @return 分页列表结果
     */
    PageList<PostVo> getPostList(PostQuery postQuery);

    /**
     * 新建
     * @param post 帖子
     */
    void insert(Post post);

    /**
     * 根据id获取详情
     * @param id 帖子id
     * @return 帖子
     */
    PostVo selectById(Long id, Long userId);

    /**
     * 点赞/取消点赞
     * @param postId 帖子id
     * @param userId 用户id
     */
    JsonResult likePost(Long postId, Long userId);

    /**
     * 检查帖子是否不存在
     * @param postId 帖子id
     * @return 是否不存在
     */
    boolean postNotExists(Long postId);

    /**
     * 根据创建人、帖子id删除帖子
     * @param postId 帖子id
     * @param userId 创建人id
     */
    JsonResult deletePost(Long postId, Long userId);

    /**
     * 获取点赞列表
     * @param postQuery 查询参数
     */
    PageList<PostVo> getLikePostList(PostQuery postQuery);

    /**
     * 拉黑/取消拉黑帖子
     * @param postId 帖子id
     * @param userId 用户id
     */
    JsonResult blockPost(Long postId, Long userId);

    /**
     * 拉黑列表
     * @param postQuery 查询参数
     */
    PageList<PostVo> getBlockPostList(PostQuery postQuery);

    /**
     * 收藏
     * @param postId 帖子id
     * @param favoriteId 收藏夹id
     * @param userId 当前用户id
     */
    JsonResult collectPost(Long postId, Long favoriteId, Long userId);

    /**
     * 收藏夹帖子列表
     * @param postQuery 查询参数
     */
    JsonResult getCollectPostPageList(PostQuery postQuery);
}
