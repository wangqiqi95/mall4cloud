package com.mall4j.cloud.api.biz.dto.channels.request.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 批量新增商品Dto
 * @Author axin
 * @Date 2023-02-13 16:38
 **/
@Data
public class BatchAddReq {
    /**
     * 商品推广类别
     * 1	普通推广商品
     * 2	定向推广商品
     * 3	专属推广商品
     */
    private Integer type;

    /**
     * 商品列表
     */
    private List<ProductParam> list;

    /**
     * 视频号Id列表
     */
    @JsonProperty("finder_ids")
    private List<String> finderIds;

    /**
     * 推广开始时间戳
     */
    @JsonProperty("begin_time")
    private Long beginTime;

    /**
     * 推广结束时间戳
     */
    @JsonProperty("end_time")
    private Long endTime;

    @JsonProperty("is_forerver")
    private Boolean isForerver;


    @Data
    public static class ProductParam{
        /**
         * 商品Id
         */
        @JsonProperty("product_id")
        private String productId;


        /**
         * 推广佣金[0, 90]%
         */
        private Integer ratio;
    }
}
