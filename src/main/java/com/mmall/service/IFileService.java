package com.mmall.service;

import com.mmall.common.ResultMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by panyuanyuan on 2017/7/1.
 */
public interface IFileService {

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    String uploadFile(MultipartFile file, String path);
}
