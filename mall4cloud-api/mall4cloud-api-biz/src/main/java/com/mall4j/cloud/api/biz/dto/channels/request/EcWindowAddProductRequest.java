package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

/**
 * 视频号4.0 橱窗上架商品request
 * @date 2023/3/8
 */
@Data
public class EcWindowAddProductRequest {
    /**
     * 商品ID
     */
    private String product_id;

    /**
     * 店铺appId
     */
    private String appid;

    /**
     * 是否需要在个人橱窗页隐藏 (默认为false)
     */
    private Boolean is_hide_for_window;
}
