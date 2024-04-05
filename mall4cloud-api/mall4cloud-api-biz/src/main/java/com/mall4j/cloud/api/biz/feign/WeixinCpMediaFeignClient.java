package com.mall4j.cloud.api.biz.feign;


import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mall4cloud-biz",contextId = "weixinCpMedia")
public interface WeixinCpMediaFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/excel/upload/by/urlFile")
    ServerResponseEntity<WeixinUploadMediaResultVO> uploadByUrlFile(@RequestBody UploadUrlMediaDTO uploadUrlMediaDTO) ;


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/upload/and/merge/suncode")
    ServerResponseEntity<WeixinUploadMediaResultVO> uploadEventCodeFile(@RequestBody UploadUrlMediaDTO uploadUrlMediaDTO);
}
