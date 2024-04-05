package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.mall4j.cloud.api.biz.dto.channels.request.EcUploadImgRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcUploadResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcBasicsApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.service.channels.ChannelsFileService;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/3/17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelsFileServiceImpl implements ChannelsFileService {

    private final WxConfig wxConfig;

    private final EcBasicsApi ecBasicsApi;

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    @Override
    public void getMedia(String mediaId, HttpServletResponse response){
        Map<String, String> map = new HashMap<>();
        String wxEcToken = wxConfig.getWxEcToken();
        map.put("access_token", wxEcToken);
        map.put("media_id", mediaId);
        HttpRequest get = HttpUtil.createGet("https://api.weixin.qq.com/channels/ec/basics/media/get");
        get.formStr(map);

        log.info("getMedia Request param : [{}]", map);
        HttpResponse httpResponse = get.execute();
        InputStream inputStream = httpResponse.bodyStream();

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new LuckException("获取IO异常");
        }
        //log.info("getMedia response：[{}]",httpResponse.body());
        IoUtil.write(outputStream,true, IoUtil.readBytes(inputStream));
    }

    @Override
    public EcUploadResponse uploadMedia(String url) {
        EcUploadImgRequest request = new EcUploadImgRequest();
        /*
         * 有多重图片格式 `/` 表示需要单独拼接oss地址
         * `https` 表示不需要拼接
         */
        if (url.startsWith("//") ){
            url = "https:" + url;
        } else if (url.startsWith("/")) {
            url = imgDomain + url;
        }
        request.setImg_url(url);

        log.info("上传图片request: [{}]", request);
        EcUploadResponse upload = ecBasicsApi.upload(wxConfig.getWxEcToken(), 1, 0, request);
        log.info("上传图片返回media:[{}]", upload.toString());
        if (upload.getErrcode() != 0){
            Assert.faild("上传图片失败：" + upload.getErrmsg());
        }
        return upload;
    }
}
