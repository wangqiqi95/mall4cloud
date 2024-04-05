package com.mall4j.cloud.biz.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.mall4j.cloud.biz.config.MinioTemplate;
import com.mall4j.cloud.biz.config.OssConfig;
import com.mall4j.cloud.biz.constant.OssType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.InputStream;
import java.util.Objects;

@Slf4j
@Service
public class MinioFileManager {

    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private OSS ossClient;
    @Autowired
    private MinioTemplate minioTemplate;

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    public String uploadFile(InputStream inputStream,  String minioPath, String contentType){
        String url = "";
        // 阿里文件上传
        if (Objects.equals(ossConfig.getOssType(), OssType.ALI.value())) {
            minioPath = minioPath.startsWith("/") ? minioPath.substring(1) : minioPath;
            ossClient.putObject(new PutObjectRequest(ossConfig.getBucket(), minioPath, inputStream));
            url =  imgDomain+"/"+minioPath;
        }
        // minio文件上传
        else if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            url = minioTemplate.uploadStream(minioPath, inputStream, contentType);
        } else {
            throw new LuckException("文件格式不支持");
        }
        return url;
    }

}
