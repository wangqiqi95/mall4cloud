package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

/**
 * 视频号4.0 橱窗获取商品
 * @date 2023/3/8
 */
@Data
public class EcWindowGetProductRequest {

    /**
     * 商品ID
     */
    private String product_id;

    /**
     * appId
     */
    private String appid;
}
