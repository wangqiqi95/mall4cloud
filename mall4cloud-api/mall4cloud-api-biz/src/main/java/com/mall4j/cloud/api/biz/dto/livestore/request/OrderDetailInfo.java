package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailInfo {

    /**
     * 商品列表
     */
    @JsonProperty("product_infos")
    private List<OrderProductInfo> productInfos;

    /**
     * 价格信息
     */
    @JsonProperty("price_info")
    private PriceInfo priceInfo;

    /**
     * fund_type = 0 必传
     */
    @JsonProperty("pay_info")
    private PayInfo payInfo;

    /**
     * 推广员分享员信息
     */
    @JsonProperty("promotion_info")
    private PromotionInfo promotionInfo;


}
