package com.example.qasystem.org.service.impl;

import com.example.qasystem.org.domain.dto.ImageData;
import com.example.qasystem.org.domain.entity.Image;
import com.example.qasystem.org.mapper.ImageMapper;
import com.example.qasystem.org.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class IImageServiceImpl implements IImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Value("${file.image-upload-path}")
    private String imageUploadPath;


    @Value("${file.image-pattern-path}")
    private String pathPattern;

    @Override
    @Transactional
    public ImageData uploadImage(MultipartFile imageFile) throws IOException {
        File dest = null;
        ImageData imageData = new ImageData();
        try {
            // 获取原始文件名
            String originalFileName = imageFile.getOriginalFilename();
            // 构建存储文件的路径
            String imagePath = imageUploadPath + originalFileName;
            // 创建目标文件对象
            dest = new File(imagePath);

            // 检查目录是否存在，如果不存在则创建
            File directory = dest.getParentFile();
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    log.error("图片上传目录创建失败");
                }
            }
            String serverIpAddress;
            try {
                serverIpAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // 处理异常
                serverIpAddress = "localhost";
            }
            // 将上传的文件保存到目标文件中
            imageFile.transferTo(dest);

            String separator = "/";
            String path = "http://" + serverIpAddress + ":8080" + separator + pathPattern + separator + originalFileName;

            Image image = new Image();
            image.setImageUrl(path);
            image.setImageStatus(0);
            image.setImageCreatedDate(Calendar.getInstance().getTime());

            // 将文件路径保存到数据库中
            Long id = imageMapper.insertImage(image);

            imageData.setId(id);
            imageData.setUrl(path);
            return imageData;
        } catch (IOException e) {
            // 发生异常时删除文件
            if (dest.exists()) {
                try {
                    boolean deleted = dest.delete();
                    if (!deleted) {
                        log.info("图片上传失败后，未删除任何资源");
                    }
                } catch (Exception ex) {
                    throw e;
                }
            }
            throw e; // 继续抛出异常，使事务回滚
        }
    }



}
