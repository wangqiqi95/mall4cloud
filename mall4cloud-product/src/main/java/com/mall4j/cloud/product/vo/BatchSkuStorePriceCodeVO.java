package com.mall4j.cloud.product.vo;

import lombok.Data;

/**
 * 电子价签管理VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@Data
public class BatchSkuStorePriceCodeVO {
    private static final long serialVersionUID = 1L;

    private String priceCode;

    private Integer status;


}
