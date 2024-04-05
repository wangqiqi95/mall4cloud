package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.CouponInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcStockInfo;
import lombok.Data;

/**
 * 视频号4.0优惠券实体
 */
@Data
public class EcCoupon {
	
	//优惠券ID
	@JsonProperty("coupon_id")
	private String couponId;
	
	//优惠券类型
	private Integer type;
	
	//优惠券状态
	private Integer status;
	
	//优惠券创建时间
	@JsonProperty("create_time")
	private Long createTime;
	
	//优惠券更新时间
	@JsonProperty("update_time")
	private Long updateTime;
	
	//优惠券信息实体
	@JsonProperty("coupon_info")
	private CouponInfo couponInfo;
	
	//优惠券库存信息实体
	@JsonProperty("stock_info")
	private EcStockInfo stockInfo;
}
