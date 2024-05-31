package com.play.playsystem.file.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.file.domain.entity.UploadFile;
import com.play.playsystem.file.mapper.UploadFileMapper;
import com.play.playsystem.file.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Value("${file.upload-path}")
    private String fileUploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadFile uploadFile(Long uploader, MultipartFile multipartFile, String typeDirectory) throws IOException {
        java.io.File dest = null;
        try {
            // 获取原始文件名
            String originalFileName = multipartFile.getOriginalFilename();
            //获取主文件名与扩展名
            String primaryName = FileUtil.mainName(multipartFile.getOriginalFilename());
            String extension = FileUtil.extName(multipartFile.getOriginalFilename());
            // 文件类型
            String fileType = MyFileUtil.getFileType(extension);
            // 设置存储文件名
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssS");
            String fileName = date.format(format) + "-" + originalFileName;
            // 存储路径
            String filePath;
            if (typeDirectory == null || typeDirectory.isEmpty()) {
                filePath = fileUploadPath + fileName;
            } else {
                filePath = fileUploadPath + typeDirectory + fileName;
            }

            // 创建目标文件对象
            dest = new java.io.File(filePath);

            // 检查目录是否存在，如果不存在则创建
            java.io.File directory = dest.getParentFile();
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    log.error("文件上传目录创建失败");
                }
            }
            // 将上传的文件保存到目标文件中
            multipartFile.transferTo(dest);
            // 将文件数据保存数据库
            UploadFile uploadFile = new UploadFile(
                    null,
                    originalFileName,
                    fileName,
                    primaryName,
                    extension,
                    filePath,
                    typeDirectory + fileName,
                    fileType,
                    multipartFile.getSize(),
                    uploader,
                    LocalDateTime.now()
            );
            if (uploadFileMapper.insert(uploadFile) > 0){
                return uploadFile;
            }
            throw new RuntimeException("上传文件失败");
        } catch (IOException e) {
            // 发生异常时删除文件
            if (dest.exists()) {
                try {
                    boolean deleted = dest.delete();
                    if (!deleted) {
                        log.info("文件上传失败后，未删除任何资源");
                    }
                } catch (Exception ex) {
                    throw e;
                }
            }
            throw e; // 继续抛出异常，使事务回滚
        }
    }

    @Override
    public UploadFile getUploadFileByUrl(String url) {
        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UploadFile::getUrl, url);
        return uploadFileMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteById(Long id) {
        uploadFileMapper.deleteById(id);
    }
}
