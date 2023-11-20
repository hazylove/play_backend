package com.example.qasystem.org.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Image")
public class Image {
    private Long id;
    private String imageUrl;
    private Long imageCreatedId;
    private int imageStatus;
    private Date imageCreatedDate;
}
