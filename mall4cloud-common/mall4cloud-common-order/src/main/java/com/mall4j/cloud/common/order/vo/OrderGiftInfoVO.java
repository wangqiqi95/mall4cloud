package com.mall4j.cloud.common.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjie
 */
@Data
public class OrderGiftInfoVO {

    private Integer isChoose;

    private String spuName;

    private String imgUrl;

    private Long spuId;

    private Integer stock;

    private Integer giftActivityId;

    private Long  skuId;

    private String skuName;

    private Integer num;

    private Long priceFee;

    private List<GiftSkuVO> giftSkuVOList;

}
