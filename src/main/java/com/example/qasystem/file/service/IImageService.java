package com.example.qasystem.file.service;

import com.example.qasystem.file.domain.dto.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    ImageData uploadImageResource(MultipartFile imageFile) throws IOException;

}
