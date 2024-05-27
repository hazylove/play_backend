package com.play.playsystem.file.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
    private Long id;
    private String url;     // 图片src ，必须
    private String alt;     // 图片描述 非必须
    private String href;    // 图片链接 非必须
}
