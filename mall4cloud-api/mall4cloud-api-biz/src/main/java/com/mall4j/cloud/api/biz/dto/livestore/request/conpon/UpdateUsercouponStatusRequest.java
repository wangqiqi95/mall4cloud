package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class UpdateUsercouponStatusRequest {

    private String openid;
    private String out_coupon_id;
    private String out_user_coupon_id;
    private Integer status;

}
