package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

/**
 * 视频号4.0 橱窗商品合集类
 * @date 2023/3/8
 */
@Data
public class EcWindowProducts {
    /**
     * 商品ID
     */
    private String product_id;

    /**
     * 商品来源店铺的appid
     */
    private String appid;
}
