package com.mall4j.cloud.api.product.vo;

import lombok.Data;

/**
 * 积分礼品VO
 * @author Grady_Lu
 */
@Data
public class ScoreProductVO {

    /**
     * 商品spuId
     */
    private Long spuId;

    /**
     * 门店编码
     */
    private String storeCode;
}
