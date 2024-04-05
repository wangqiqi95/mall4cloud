package com.mall4j.cloud.api.biz.dto.channels.request.league.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 删除联盟商品
 * @Author axin
 * @Date 2023-02-13 17:46
 **/
@Data
public class ItemDeleteReq {
    /**
     * 商品id type为普通推广商品时必填
     */
    @JsonProperty("product_id")
    private String productId;

    /**
     * 特殊推广商品计划id
     * type为特殊推广商品时必填
     */
    @JsonProperty("info_id")
    private String infoId;

    /**
     * 1	普通推广商品
     * 2	定向推广商品
     * 3	专属推广商品
     */
    private Integer type;
}
