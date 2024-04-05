package com.mall4j.cloud.biz.controller.wx.channels;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.dto.channels.response.EcUploadResponse;
import com.mall4j.cloud.biz.service.channels.ChannelsFileService;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 视频号4.0文件相关
 * @date 2023/3/17
 */
@RestController("multishopChannelsFileController")
@RequestMapping("/ua/channels/file")
@Api(tags = "视频号4.0文件相关")
public class ChannelsFileController {

    @Autowired
    private ChannelsFileService channelsFileService;

    /**
     * 返回的是二进制流文件
     * @param mediaId
     * @param response
     * @return
     */
    @PostMapping("/media/get")
    public ServerResponseEntity<Void> getMedia(@RequestBody String mediaId, HttpServletResponse response){
        channelsFileService.getMedia(mediaId, response);
        return ServerResponseEntity.success();
    }

    /**
     * 文件地址上传图片
     * @param url
     * @return media_id
     */
    @PostMapping("/media/upload")
    public ServerResponseEntity<EcUploadResponse> uploadMedia(@RequestParam String url){
        if (StrUtil.isBlankIfStr(url)){
            Assert.faild("传入参数为空");
        }
        EcUploadResponse response = channelsFileService.uploadMedia(url);
        return ServerResponseEntity.success(response);
    }
}
