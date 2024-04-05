package com.mall4j.cloud.api.biz.dto.channels.response.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 推广商品列表
 * @Author axin
 * @Date 2023-02-14 10:42
 **/
@Data
public class ListGetResp extends BaseResponse {
    /**
     * 商品id
     */
    @JsonProperty("product_id")
    private List<String> productId;

    /**
     * 特殊推广商品计划id
     */
    @JsonProperty("info_id")
    private List<String> infoId;

    /**
     * 本次翻页的上下文，用于顺序翻页请求
     */
    @JsonProperty("last_buffer")
    private String lastBuffer;
    /**
     * 商品总数
     */
    @JsonProperty("total_num")
    private Integer totalNum;
    /**
     * 是否还有剩余商品
     */
    @JsonProperty("has_more")
    private Boolean hasMore;
}
