package com.example.qasystem.org.controller;

import com.example.qasystem.basic.utils.JsonResult;
import com.example.qasystem.org.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/img")
@Slf4j
public class ImageController {

    @Autowired
    private IImageService iImageService;

    @PostMapping("/upload")
    public JsonResult uploadImage(@RequestParam MultipartFile imageFile){
        try {
            iImageService.uploadImage(imageFile);
        }  catch (IOException e) {
            log.error("文件上传出现异常：", e);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("文件上传出现异常");
        } catch (Exception e) {
            log.error("文件上传出现异常：", e);
            return new JsonResult().setCode(500).setSuccess(false).setMassage("服务器错误，上传失败");
        }

        return new JsonResult().setMassage("上传成功");
    }
}
