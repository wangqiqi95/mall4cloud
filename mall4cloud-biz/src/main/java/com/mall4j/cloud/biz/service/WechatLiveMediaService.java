package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.live.WxInterfaceInfo;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveMediaService {

    /**
     * 上传图片
     * @param imgUrl
     * @return 图片的临时url
     */
    String uploadImage(String imgUrl);

    /**
     * 获取图片 mediaId 有效期三天
     * @param imgUrl 地址
     * @return mediaId
     */
    String getImageMediaId(String imgUrl);

}
