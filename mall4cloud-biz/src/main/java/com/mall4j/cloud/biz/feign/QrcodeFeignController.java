package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.QrcodeFeignClient;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author cl
 * @date 2021-08-13 15:50:02
 */
@RestController
@Slf4j
public class QrcodeFeignController implements QrcodeFeignClient {

    @Autowired
    private QrcodeTicketService qrcodeTicketService;

    /**
     * 小程序短链生成
     * @param path
     * @param isExpire
     * @return
     */
    @Override
    public ServerResponseEntity<String> generateUrlLink(String path, boolean isExpire,String id,Integer expireTime) {
        return qrcodeTicketService.generateUrlLink(path,isExpire,id,expireTime);
    }

    /**
     * 小程序短链生成(链接转二维码，二维码图片base64编码)
     * @param path
     * @param isExpire
     * @return
     */
    @Override
    public ServerResponseEntity<String> generateQrcodeUrlLink(String path, boolean isExpire, String id, Integer qrCodeSize,Integer expireTime) {
        return qrcodeTicketService.generateQrcodeUrlLink(path,isExpire,id,qrCodeSize,expireTime);
    }

    @Override
    public ServerResponseEntity<String> getWxaCodeBaseImg(String scene, String path, Integer width) {
        return qrcodeTicketService.getWxaCodeBaseImg(scene,path,width);
    }

    @Override
    public ServerResponseEntity<String> getWxaCodeUrl(String scene, Long storeId, String path, Integer width) {
        return qrcodeTicketService.getWxaCodeUrl(scene,path,width,storeId);
    }

    @Override
    public ServerResponseEntity<File> getWxaCodeFile(String scene, String path, Integer width) {
        return qrcodeTicketService.getWxaCodeFile(scene,path,width);
    }

}
