package com.mall4j.cloud.api.biz.dto.channels.request.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 更新联盟商品
 * @Author axin
 * @Date 2023-02-13 17:10
 **/
@Data
public class ItemUpdReq {
    /**
     * 商品id type为普通推广商品时必填
     */
    @JsonProperty("product_id")
    private String productId;

    /**
     * 特殊推广商品计划id  type为特殊推广商品时必填
     */
    @JsonProperty("info_id")
    private String infoId;

    /**
     * 商品推广类别
     * 1	普通推广商品
     * 2	定向推广商品
     * 3	专属推广商品
     */
    private Integer type;

    /**
     * 更新操作类别
     * 1	编辑并上架
     * 2	下架
     * 4	上架
     */
    @JsonProperty("operate_type")
    private Integer operateType;

    /**
     * 推广佣金[0, 90]%
     */
    private Integer ratio;

    /**
     * 特殊推广信息  类别为特殊推广商品且操作为编辑时必
     */
    @JsonProperty("exclusive_info")
    private ExclusiveInfo exclusiveInfo;


    @Data
    public static class ExclusiveInfo {
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
         * 新增推广达人视频号列表，不超过30个
         */
        @JsonProperty("add_finder_ids")
        private List<String> addFinderIds;

        /**
         * 删除推广达人视频号列表，不超过30个
         */
        @JsonProperty("del_finder_ids")
        private List<String> delFinderIds;

        /**
         * 是否永久推广
         */
        @JsonProperty("is_forerver")
        private Boolean isForerver;
    }

}
