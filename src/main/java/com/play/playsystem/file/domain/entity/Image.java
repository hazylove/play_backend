package com.play.playsystem.file.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_image")
public class Image {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String imageUrl;

    private Long imageCreatedId;

    private int imageStatus;

    private Date imageCreatedDate;
}
