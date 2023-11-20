package com.example.qasystem.org.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    void uploadImage(MultipartFile imageFile) throws IOException;
}
