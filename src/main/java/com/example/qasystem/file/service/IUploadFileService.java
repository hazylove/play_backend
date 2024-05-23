package com.example.qasystem.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.qasystem.file.domain.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploadFileService extends IService<UploadFile> {

    UploadFile uploadFile(Long uploader, MultipartFile multipartFile, String typeDirectory) throws IOException;

    UploadFile getUploadFileByUrl(String url);

    void deleteById(Long id);
}
