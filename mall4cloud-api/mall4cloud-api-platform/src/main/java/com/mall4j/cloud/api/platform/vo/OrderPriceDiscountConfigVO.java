package com.mall4j.cloud.api.platform.vo;

import lombok.Data;

/**
 * 系统配置信息表VO
 *
 * @author lhd
 * @date 2021-04-20 16:27:57
 */
@Data
public class OrderPriceDiscountConfigVO {
    private static final long serialVersionUID = 1L;


    private String discount;
    private String email;
    private String subject;
    private String content;
    private String couponIds;

    private String priceDiscount;
    private String priceCodeCount;

    private String payOrderDiscount;//下单价格兜底折扣比例

}
