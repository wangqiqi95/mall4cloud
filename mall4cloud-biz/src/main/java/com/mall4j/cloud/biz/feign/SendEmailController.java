package com.mall4j.cloud.biz.feign;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.feign.SendEmailFeignClient;
import com.mall4j.cloud.biz.service.SendEmailService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
@RestController
@RequiredArgsConstructor
public class SendEmailController implements SendEmailFeignClient {

    @Autowired
    private SendEmailService service;
    @Override
    public ServerResponseEntity<String> sendEmailToFileUrl(String fileUrl, String fromSubject,String fromContent,String receiveEmail) {
        return service.sendEmailToFileUrl(fileUrl,fromSubject,fromContent,receiveEmail);
    }
}
