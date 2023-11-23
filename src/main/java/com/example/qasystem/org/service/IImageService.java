package com.example.qasystem.org.service;

import com.example.qasystem.org.domain.dto.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    ImageData uploadImage(MultipartFile imageFile) throws IOException;
}
