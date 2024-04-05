package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class AddUserCouponRequest {

    private String openid;
    private Long recv_time;
    private UserCoupon user_coupon;
}
