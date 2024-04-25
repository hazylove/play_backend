package com.example.qasystem.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.qasystem.file.domain.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ImageMapper extends BaseMapper<Image> {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long insertImage(Image image);
}
