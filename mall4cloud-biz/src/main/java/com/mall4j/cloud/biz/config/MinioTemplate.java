package com.mall4j.cloud.biz.config;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author FrozenWatermelon
 */
@Slf4j
@Component
public class MinioTemplate implements InitializingBean {

    @Autowired
    private OssConfig ossConfig;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() {
        this.minioClient =  MinioClient.builder().endpoint(ossConfig.getEndpoint())
                .credentials(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret())
                .build();
    }


    /**
     * 删除文件
     *
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String objectName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().object(objectName).bucket(ossConfig.getBucket()).build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除文件
     * @param filePaths 文件名称数组
     */
    public void removeObjects(List<String> filePaths) {
        List<DeleteObject> deleteObjects = new ArrayList<>(filePaths.size());
        filePaths.forEach(item -> {
            deleteObjects.add(new DeleteObject(item));
        });
        minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                .bucket(ossConfig.getBucket())
                .objects(deleteObjects)
                .build()
        );
    }

    /**
     * 获得上传的URL
     * @param objectName
     */
    public String getPresignedObjectUrl(String objectName){
        try {
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(ossConfig.getBucket()).object(objectName).expiry(10, TimeUnit.MINUTES).method(Method.PUT).build());
            return presignedObjectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }


    /**
     * 方法描述：文件上传
     * @param minioFilePath         文件分成文件夹的形式，只需在文件名前加入对应的文件夹路径就行了
     * @param inputStream           文件流
     * @param contentType           
     * @return java.lang.String
     * @date 2021-12-15 19:40:49
     */
    public String uploadStream( String minioFilePath, InputStream inputStream, String contentType) {
        if (StringUtils.isBlank(contentType)) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(ossConfig.getBucket()).object(minioFilePath).contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
            return new StringBuilder(ossConfig.getEndpoint()).append("/").append(ossConfig.getBucket()).append("/").append(minioFilePath).toString();
        } catch (Exception e) {
            log.info("文件上传失败 {} {}",e,e.getMessage());
            throw new LuckException("文件上传失败");
        }
    }

}
