package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * 视频号4.0获取优惠券响应实体
 */
@Data
public class EcCouponGetResponse extends BaseResponse {
	//优惠券实体
	private EcCoupon coupon;

}
