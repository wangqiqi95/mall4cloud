package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

/**
 * 视频号4.0 橱窗商品下架
 * @date 2023/3/8
 */
@Data
public class EcWindowOffProductRequest {
    /**
     * 橱窗商品ID
     */
    private String product_id;

    /**
     * 商品来源店铺的appid
     */
    private String appid;
}
