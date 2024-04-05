package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 视频号4.0 橱窗商品抢购信息
 * @date 2023/3/8
 */
@Data
public class EcWindowLimitDiscountInfo {

    /**
     * 是否有生效中的抢购活动
     */
    private Boolean is_effect;

    /**
     * 抢购价
     */
    private BigDecimal discount_price;

    /**
     * 抢购活动结束时间(毫秒时间戳)
     */
    private String end_time_ms;

    /**
     * 抢购剩余库存
     */
    private Integer stock;
}
