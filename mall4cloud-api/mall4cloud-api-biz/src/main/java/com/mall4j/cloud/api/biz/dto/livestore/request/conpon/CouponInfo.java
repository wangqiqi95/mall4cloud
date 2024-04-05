package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class CouponInfo {
    //优惠券名
    private String name;
    //优惠券推广类型
    private Integer promote_type;

    private PromoteInfo promote_info;
    private DiscountInfo discount_info;
    private ReceiveInfo receive_info;
    private ValidInfo valid_info;

}
