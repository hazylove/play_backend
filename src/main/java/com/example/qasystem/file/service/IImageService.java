package com.example.qasystem.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.file.domain.dto.ImageData;
import com.example.qasystem.file.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService extends IService<Image> {
    ImageData uploadImage(MultipartFile imageFile) throws IOException;
}
