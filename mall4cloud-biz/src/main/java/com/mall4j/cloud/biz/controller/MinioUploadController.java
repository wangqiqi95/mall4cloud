package com.mall4j.cloud.biz.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.biz.config.MinioTemplate;
import com.mall4j.cloud.biz.config.OssConfig;
import com.mall4j.cloud.biz.constant.OssType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RefreshScope
public class MinioUploadController implements MinioUploadFeignClient {
	private static final Logger log = LoggerFactory.getLogger(MinioUploadController.class);

	@Autowired
	private OssConfig ossConfig;
//	@Autowired
//	private AwsS3Template awsS3Template;
	@Autowired
	private OSS ossClient;
	@Autowired
	private MinioTemplate minioTemplate;

	@Value("${biz.oss.resources-url}")
	private String imgDomain;

	@Value("${aws.openaws}")
	private Boolean openaws=false;

	@Override
	public ServerResponseEntity<String> minioFileUpload(MultipartFile file, String minioPath, String contentType) {
		try {
			String url = "";
			if(openaws){
				//亚马逊S3文件上传
//				url=awsS3Template.uploadPublic(file);
			}else if (Objects.equals(ossConfig.getOssType(), OssType.ALI.value())) {
				// 阿里文件上传
				minioPath = minioPath.startsWith("/") ? minioPath.substring(1) : minioPath;
				ossClient.putObject(new PutObjectRequest(ossConfig.getBucket(), minioPath, file.getInputStream()));
				url =  imgDomain+"/"+minioPath;
			}
			// minio文件上传
			else if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
				url = minioTemplate.uploadStream(minioPath, file.getInputStream(), StringUtils.isNotBlank(contentType) ? contentType : file.getContentType());
			} else {
				return ServerResponseEntity.showFailMsg("不支持的文件上传类型");
			}
			return ServerResponseEntity.success(url);
		} catch (Exception e) {
			log.error(minioPath + "文件上传异常", e);
			throw new LuckException("文件上传失败");
		} finally {
			log.info("文件上传请求结束");
		}
	}
}
