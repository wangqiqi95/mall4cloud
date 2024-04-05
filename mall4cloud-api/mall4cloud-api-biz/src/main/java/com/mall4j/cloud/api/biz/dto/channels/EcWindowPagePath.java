package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

/**
 * 视频号4.0 橱窗详情页，半屏页，全屏页地址
 * @date 2023/3/8
 */
@Data
public class EcWindowPagePath {

    /**
     * 商品详情半屏页、全屏页所属appid
     */
    private String appid;

    /**
     * 商品详情半屏页path
     */
    private String half_page_path;

    /**
     * 商品详情全屏页path
     */
    private String full_page_path;
}
