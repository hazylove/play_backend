package com.play.playsystem.file.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_upload_file")
public class UploadFile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件实际名称
     */
    private String realName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件主名称
     */
    private String primaryName;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 存放路径
     */
    private String path;

    /**
     * url
     */
    private String url;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 上传人
     */
    private Long uploader;

    /**
     * 文件上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate; // 上传时间

}
