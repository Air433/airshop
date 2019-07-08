package com.airshop.upload.controller;

import com.airshop.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author ouyanggang
 * @Date 2019/7/5 - 11:21
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String url = this.uploadService.upload(file);

        if (StringUtils.isBlank(url)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(url);
    }

    @PostMapping("test-image")
    public ResponseEntity<String> testImage(@RequestParam("file") MultipartFile file) throws IOException {

        return new ResponseEntity("http://192.168.13.132:80/group1/M00/00/00/wKgNhF0hvJmAecsJAABigdr8PSA597.png", HttpStatus.OK);
    }
}
