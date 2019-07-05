package com.airshop.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author ouyanggang
 * @Date 2019/7/5 - 11:23
 */
public interface UploadService {

    /**
     * 文件上传
     * @param file
     * @return
     */
    String upload(MultipartFile file) throws IOException;
}
