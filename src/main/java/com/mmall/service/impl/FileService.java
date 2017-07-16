package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import java.io.File;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作Service
 *
 * Created by panyuanyuan on 2017/7/1.
 */
@Service
public class FileService implements IFileService{

    private static final Logger logger = LoggerFactory.getLogger(ProdcutService.class);

    @Override
    public String uploadFile(MultipartFile file, String path) {

        String fileName = file.getOriginalFilename();
        String fileExtention = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = new StringBuilder().append(UUID.randomUUID().toString()).append(".").append(fileExtention).toString();
        File targetFile = new File(path, uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        try {
            //上传到本地服务器
            file.transferTo(targetFile);
            //上传文件到FTP
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除本地文件
            targetFile.delete();
        } catch (Exception e) {
            logger.error("upload file to upload server err");
        }

        return targetFile.getName();
    }
}
