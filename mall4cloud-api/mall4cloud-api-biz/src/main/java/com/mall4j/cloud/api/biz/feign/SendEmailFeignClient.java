package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author gmq
 * @Date 2022/02/14 15:19
 */
@FeignClient(value = "mall4cloud-biz",contextId = "sendEmailApi")
public interface SendEmailFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sendemailapi/sendEmailToFileUrl")
    ServerResponseEntity<String> sendEmailToFileUrl(@RequestParam(value = "fileUrl",required = true) String fileUrl,
                                                    @RequestParam(value = "fromSubject",required = true) String fromSubject,
                                                    @RequestParam(value = "fromContent",required = true) String fromContent,
                                                    @RequestParam(value = "receiveEmail",required = true) String receiveEmail) ;




}
