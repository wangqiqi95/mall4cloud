package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

/**
 * 会员订单信息VO
 */
@Data
public class MemberOrderInfoBO {
	
	//会员Id
	private  Long userId;
	
	//订单实付金额
	private Long actualTotal;

}
