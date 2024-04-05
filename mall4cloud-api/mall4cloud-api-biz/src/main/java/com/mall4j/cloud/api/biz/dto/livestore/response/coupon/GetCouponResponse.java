package com.mall4j.cloud.api.biz.dto.livestore.response.coupon;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.conpon.Coupon;
import lombok.Data;

@Data
public class GetCouponResponse extends BaseResponse {
    private GetCouponResult result;
}
