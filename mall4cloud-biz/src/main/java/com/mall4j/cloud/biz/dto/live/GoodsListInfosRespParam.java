package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

@Data
public class GoodsListInfosRespParam {
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 	商品图片url
     */
    private String coverImgUrl;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品详情页的小程序路径
     */
    private String url;
    /**
     * 1:一口价，此时读price字段; 2:价格区间，此时price字段为左边界，price2字段为右边界; 3:折扣价，此时price字段为原价，price2字段为现价；
     */
    private Integer priceType;
    /**
     * 价格左区间，单位“元”
     */
    private Double price;
    /**
     * 价格右区间，单位“元”
     */
    private Double price2;
    /**
     * 1、2：表示是为 API 添加商品，否则是直播控制台添加的商品
     */
    private Integer thirdPartyTag;
}
