package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("liveStoreImgController")
@RequestMapping("/ua/livestore/img")
@Api(tags = "视频号图片管理")
public class LiveStoreImgController {


    @Autowired
    WechatLiveMediaService mediaService;

    @ApiOperation("纠纷单，上传凭证详情前上传图片。需传图片的url完整路径")
    @GetMapping("/upload")
    public ServerResponseEntity<String> upload(String url) {
        return ServerResponseEntity.success(mediaService.uploadImage(url));
    }



}
