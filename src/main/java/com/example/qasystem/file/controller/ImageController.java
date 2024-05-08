package com.example.qasystem.file.controller;

import com.example.qasystem.basic.utils.result.ImgResult;
import com.example.qasystem.basic.utils.result.ResultCode;
import com.example.qasystem.file.domain.dto.ImageData;
import com.example.qasystem.file.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/img")
@Slf4j
public class ImageController {

    @Autowired
    private IImageService iImageService;

    @PostMapping("/upload")
    public ImgResult uploadImage(@RequestParam MultipartFile imageFile) {
        ImageData imageData;
        try {
            imageData = iImageService.uploadImage(imageFile);
        } catch (Exception e) {
            log.error("文件上传出现异常：", e);
            return new ImgResult().setErrno(ResultCode.IMAGE_ERROR_CODE).setMessage("文件上传失败");
        }
        return new ImgResult().setErrno(ResultCode.IMAGE_SUCCESS_CODE).setData(imageData);
    }
}
