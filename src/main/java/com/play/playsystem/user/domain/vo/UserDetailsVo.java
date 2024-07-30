package com.play.playsystem.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsVo {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 简介
     */
    private String profile;

    /**
     * 出生日期
     */
    private LocalDate birth;

    /**
     * 关注数量
     */
    private int followerCount;

    /**
     * 粉丝数量
     */
    private int fansCount;

    /**
     * 好友数量
     */
    private int friendCount;

    /**
     * 发帖数量
     */
    private int postCount;

    /**
     * 点赞数量
     */
    private int likeCount;

    /**
     * 收藏夹数量
     */
    private int favoritesCount;

    /**
     * 根据User实体构造UserDetailsVo
     * @param user 用户实体
     */
    public UserDetailsVo(final User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.avatar = MyFileUtil.reSetFileUrl(user.getAvatar());
            this.createdDate = user.getCreatedDate();
            this.gender = user.getGender();
            this.age = user.getAge();
            this.birth = user.getBirth();
            this.profile = user.getProfile();
        }
    }
}
