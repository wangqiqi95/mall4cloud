package com.mall4j.cloud.api.biz.dto.channels.response.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * @Description 分享员绑定出参
 * @Author axin
 * @Date 2023-02-14 11:36
 **/
@Data
public class SharerBindResp extends BaseResponse {

    /**
     * 邀请二维码的图片二进制，3天有效
     */
    @JsonProperty("qrcode_img")
    private String qrcodeImg;

    /**
     * 邀请二维码的图片二进制base64编码，3天有效
     */
    @JsonProperty("qrcode_img_base64")
    private String qrcodeImgBase64;
}
