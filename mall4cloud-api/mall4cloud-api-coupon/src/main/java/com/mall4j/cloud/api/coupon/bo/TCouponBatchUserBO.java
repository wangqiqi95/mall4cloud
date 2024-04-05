package com.mall4j.cloud.api.coupon.bo;

import lombok.Data;

import java.util.List;

/**
 * 批量分发优惠券信息
 * @author shijing
 */
@Data
public class TCouponBatchUserBO {

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 活动来源
     */
    private Long activityId;

    /**
     * 用户id
     */
    private List<Long> userIds;
}
