package com.mall4j.cloud.api.biz.dto.channels.request.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 分享员订单列表
 * @Author axin
 * @Date 2023-02-13 14:08
 **/
@Data
public class GetSharerOrderListReq {
    /**
     * 分享员openid
     */
    private String openid;

    /**
     * 分享场景
     * 1	直播间
     * 2	橱窗
     * 3	短视频
     * 4	视频号主页
     * 5	商品详情页
     */
    @JsonProperty("share_scene")
    private Integer shareScene;

    /**
     * 分页参数，页数
     */
    private Integer page;

    /**
     * 分页参数，每页订单数（不超过100）
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * 订单创建开始时间
     */
    @JsonProperty("start_time")
    private Long startTime;

    /**
     * 订单创建结束时间
     */
    @JsonProperty("end_time")
    private Long endTime;
}
