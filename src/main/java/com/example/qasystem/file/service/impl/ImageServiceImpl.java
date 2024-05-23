package com.example.qasystem.file.service.impl;

import com.example.qasystem.file.domain.dto.ImageData;
import com.example.qasystem.file.domain.entity.UploadFile;
import com.example.qasystem.file.service.IImageService;
import com.example.qasystem.file.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class ImageServiceImpl implements IImageService {

    @Autowired
    private IUploadFileService uploadFileService;

    private final static String RICH_TEXT_IMAGE_DEST = "rich_text_img";

    /**
     *
     * @param imageFile 图片文件
     * @return 图片上传返回值
     * @throws IOException 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImageData uploadImageResource(MultipartFile imageFile) throws IOException {
        // 存储文件
        UploadFile uploadFile = uploadFileService.uploadFile(null, imageFile, RICH_TEXT_IMAGE_DEST);

        // 构建图片返回值
        ImageData imageData = new ImageData();
        imageData.setId(uploadFile.getId());
        imageData.setUrl(uploadFile.getUrl());

        return imageData;
    }
}
