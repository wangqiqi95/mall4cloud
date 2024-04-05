package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0折扣条件实体
 */
@Data
public class EcDiscountCondition {
	
	//商品件数门槛，满 x 件商品可用，不能和价格门槛同时设置
	@JsonProperty("product_cnt")
	private Integer productCnt;
	
	//商品id，商品券需填写
	@JsonProperty("product_ids")
	private List<String> productIds;
	
	//商品价格门槛，价格满 x 可用，单位分，不能和件数门槛同时设置
	@JsonProperty("product_price")
	private Integer productPrice;
}
