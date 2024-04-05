package com.mall4j.cloud.biz.dto.live;

import lombok.Data;
import org.apache.http.entity.mime.content.ByteArrayBody;

/**
 * 微信接口所需参数
 * @author LHD
 */
@Data
public class WxInterfaceInfo {

    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     */
    private String imgType = "image";

    /**
     * 微信的accessToken
     */
    private String accessToken;

    /**
     * 图片字节数组
     */
    private byte[] bytes;
    private Long limit;

    private Long start;

    private ByteArrayBody byteArray;

    public String getRequestUrl() {
        return "/upload";
    }

}
