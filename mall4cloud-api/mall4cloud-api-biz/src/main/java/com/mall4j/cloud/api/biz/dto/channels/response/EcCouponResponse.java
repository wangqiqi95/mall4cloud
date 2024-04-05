package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCouponData;
import lombok.Data;

/**
 * 视频号4.0优惠券响应实体
 */
@Data
public class EcCouponResponse extends EcBaseResponse {
	//响应数据
	private EcCouponData data;
}
