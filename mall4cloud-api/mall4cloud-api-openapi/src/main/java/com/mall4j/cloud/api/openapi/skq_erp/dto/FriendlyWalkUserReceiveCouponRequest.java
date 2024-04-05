package com.mall4j.cloud.api.openapi.skq_erp.dto;

import lombok.Data;

@Data
public class FriendlyWalkUserReceiveCouponRequest {

    /**
     * 优惠券活动ID
     */
    private Long activityId;

    /**
     * 用户 unionId
     */
    private String unionId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 用户所在门店
     */
    private Long storeId;

}
