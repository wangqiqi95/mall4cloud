package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.service.WeixinFileUploadService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 微信图片模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
@RestController("platformWeixinFileUploadController")
@RequestMapping("/p/weixin/imgupload")
@Api(tags = "微信文件上传")
@Slf4j
public class WeixinFileUploadController {

    @Autowired
    private WeixinFileUploadService weixinFileUploadService;


    @PostMapping(value = "/uploadImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public ServerResponseEntity<WxMpMaterialUploadResult> uploadImg(@RequestPart("file") MultipartFile file,@RequestParam(value = "appId",required = true) String appId) {
        return ServerResponseEntity.success(weixinFileUploadService.uploadImg(file,appId));
    }

    @PostMapping(value = "/uploadImgUrl")
    @ApiOperation(value = "上传图片url", notes = "上传图片url")
    public ServerResponseEntity<WxMpMaterialUploadResult> uploadImgUrl(@RequestParam("url") String url,@RequestParam(value = "appId",required = true) String appId) {
        return ServerResponseEntity.success(weixinFileUploadService.uploadImgUrl(url,appId));
    }


}
