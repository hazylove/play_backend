package com.example.qasystem.org.mapper;

import com.example.qasystem.org.domain.entity.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    void insertImage(Image image);
}
