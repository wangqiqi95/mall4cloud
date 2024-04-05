package com.mall4j.cloud.api.biz.dto.channels.response.league.promoter;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 优选联盟达人
 * @Author axin
 * @Date 2023-02-13 15:06
 **/
@Data
public class PromoterInfo {
    /**
     * 视频号ID
     */
    @JsonProperty("finder_id")
    private String finderId;

    /**
     * 合作状态
     * 0	初始值
     * 1	邀请中
     * 2	达人已接受邀请
     * 3	达人已拒绝邀请
     * 4	已取消邀请
     * 5	已取消合作
     * 10	已删除
     */
    private Integer status;

    /**
     * 达人邀请秒级时间戳
     */
    @JsonProperty("invite_time")
    private Long inviteTime;

    /**
     * 累计合作商品数
     */
    @JsonProperty("sale_product_number")
    private Integer saleProductNumber;

    /**
     * 合作动销GMV
     */
    @JsonProperty("sale_gmv")
    private Integer saleGmv;
}
