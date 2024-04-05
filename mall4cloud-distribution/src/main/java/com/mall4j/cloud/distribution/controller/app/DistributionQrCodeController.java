package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.config.WxConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;

/**
 *
 * @author cl
 * @date 2021-08-12 10:46:10
 */
@RestController("appDistributionQrCodeController")
@RequestMapping("/ua/distribution_qrcode")
@Api(tags = "分销生成二维码接口")
public class DistributionQrCodeController {

    @Autowired
    private WxConfig wxConfig;

    @GetMapping("/invitation")
    @ApiOperation(value="分销员邀请二维码", notes="分销员的邀请二维码带有cardNo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scene", value = "二维码携带的参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "获取二维码后跳转的页面", required = true, dataType = "String")
    })
    public ServerResponseEntity<FileSystemResource> qrCodeCreate(String page, String scene) {
        File file = null;
        try {
            // 只有发布到线上了，正式版才能调用成功，否则错误码为：41030
            file = wxConfig.getWxMaService().getQrcodeService().createWxaCodeUnlimit(scene, page);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new LuckException("获取分销员邀请二维码失败!");
        }
        return ServerResponseEntity.success(new FileSystemResource(file));
    }

}
