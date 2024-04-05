package com.mall4j.cloud.api.biz.dto.channels.request.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 获取商品推广详情
 * @Author axin
 * @Date 2023-02-13 17:58
 **/
@Data
public class ItemGetReq {
    /**
     * 获取商品推广类别
     */
    private Integer type;

    /**
     * type为普通推广商品时必填	商品id
     */
    @JsonProperty("product_id")
    private String productId;

    /**
     * type为特殊推广商品时必填	特殊推广商品计划id
     */
    @JsonProperty("info_id")
    private String infoId;

    /**
     * 是否获取特殊推广商品绑定的达人列表， type为特殊推广商品时有效
     */
    @JsonProperty("need_relation")
    private Boolean needRelation;

    /**
     * need_relation为真时必填	拉取达人数 不超过50
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * need_relation为真时有效，页面下标，下标从1开始，默认为1
     */
    @JsonProperty("page_index")
    private Integer pageIndex;

    /**
     * need_relation为真时有效，是否需要返回该计划绑定达人总数
     */
    @JsonProperty("need_total_num")
    private Boolean needTotalNum;
}
