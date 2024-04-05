package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0折扣信息实体
 */
@Data
public class EcDiscountInfo {
	
	//折扣条件实体
	@JsonProperty("discount_condition")
	private EcDiscountCondition discountCondition;
	
	//优惠减免金额，单位为分
	@JsonProperty("discount_fee")
	private Integer discountFee;
	
	//优惠减免折扣数，换算规则，5000=5折，7000=7折，范围是1000-10000，必须是100的整数
	@JsonProperty("discount_num")
	private Integer discountNum;
}
