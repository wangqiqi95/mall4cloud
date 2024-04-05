package com.mall4j.cloud.api.biz.dto.channels.response.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 商品详情
 * @Author axin
 * @Date 2023-02-13 19:20
 **/
@Data
public class Item {
    /**
     * 商品推广类别
     * 1	普通推广商品
     * 2	定向推广商品
     * 3	专属推广商品
     */
    private Integer type;

    /**
     * 商品id
     */
    @JsonProperty("product_id")
    private String productId;

    /**
     * 商品推广状态
     * 1	已上架推广
     * 2	已下架推广
     * 4	已删除
     * 5	未达到准入标准
     * 10	待生效
     */
    private Integer status;

    /**
     * 推广佣金[0, 90]%
     */
    private Integer ratio;

    /**
     * 特殊推广信息
     */
    @JsonProperty("exclusive_info")
    private ExclusiveInfo exclusiveInfo;

    /**
     * 扩展信息
     */
    @JsonProperty("ext_info")
    private ExtInfo extInfo;


    @Data
    private static class ExclusiveInfo {
        /**
         * 特殊推广商品计划id
         */
        @JsonProperty("info_id")
        private String infoId;

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

        /**
         * 是否永久推广
         */
        @JsonProperty("is_forerver")
        private Boolean isForerver;

        /**
         * 推广达人视频号列表
         */
        @JsonProperty("finder_ids")
        private List<String> finderIds;

        /**
         * 推广达人数量
         */
        @JsonProperty("finder_num")
        private Integer finderNum;

    }

    @Data
    private class ExtInfo{
        /**
         * 是否类目禁售
         */
        @JsonProperty("is_sale_forbidden")
        private Boolean isSaleForbidden;

        /**
         * 是否被官方封禁
         */
        @JsonProperty("is_banned")
        private Boolean isBanned;
    }
}
