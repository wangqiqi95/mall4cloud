package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "mall4cloud-biz",contextId = "minioFileUpload")
public interface MinioUploadFeignClient {

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ua/oss/minioFileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ServerResponseEntity<String> minioFileUpload(@RequestPart("file") MultipartFile file, @RequestParam("minioPath") String minioPath, @RequestParam("contentType") String contentType);
}
