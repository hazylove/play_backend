package com.example.qasystem.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.qasystem.file.domain.entity.UploadFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {

}
