package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class UpdateCouponStatusRequest {
    private String out_coupon_id;
    private Integer status;
}
