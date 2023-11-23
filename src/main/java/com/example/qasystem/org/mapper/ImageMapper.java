package com.example.qasystem.org.mapper;

import com.example.qasystem.org.domain.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ImageMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long insertImage(Image image);
}
