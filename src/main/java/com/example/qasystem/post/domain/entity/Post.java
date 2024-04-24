package com.example.qasystem.post.domain.entity;

import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Post")
public class Post {
    private Long id;
    private String postTitle;
    private String postContent;
    private String postTag;
    private Long postCreatedId;
    private UserBase postCreatedBy;
    private Date postCreatedDate;
    private int postCommentNum;
}
