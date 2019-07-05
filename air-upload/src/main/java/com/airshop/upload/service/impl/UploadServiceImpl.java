package com.airshop.upload.service.impl;

import com.airshop.upload.controller.UploadController;
import com.airshop.upload.service.UploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/5 - 11:24
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    public static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient;

    @Value("${fastFDS.address}")
    private String address;

    @Override
    public String upload(MultipartFile file) throws IOException {

        try {
        String type = file.getContentType();

        if (!suffixes.contains(type)){
            logger.info("上传文件失败，文件类型不匹配：{}", type);
            return null;
        }

        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null){
            logger.info("上传失败，文件内容不符合要求");
            return null;
        }

//        File dir = new File("D:\\work\\heima\\upload");
//        if (!dir.exists()){
//            dir.mkdir();
//        }
//
//        file.transferTo(new File(dir, file.getOriginalFilename()));
//
//        String url = "http://image.airshop.com/upload/"+ file.getOriginalFilename();
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(),
                    extension, null);

            return address + storePath.getFullPath();
        } catch (Exception e){
            logger.error("", e);
            return null;
        }
    }
}
