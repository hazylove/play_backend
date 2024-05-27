package com.play.playsystem.file.service;

import com.play.playsystem.file.domain.dto.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    ImageData uploadImageResource(MultipartFile imageFile) throws IOException;

}
