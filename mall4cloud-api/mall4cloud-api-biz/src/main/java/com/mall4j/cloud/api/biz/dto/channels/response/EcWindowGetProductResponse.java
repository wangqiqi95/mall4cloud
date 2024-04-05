package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcWindowProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频号4.0 橱窗获取商品
 * @date 2023/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EcWindowGetProductResponse extends EcBaseResponse{

    /**
     * 商品
     */
    private EcWindowProduct product;
}
