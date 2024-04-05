package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.providerResponse;

import lombok.Data;

/**
 * 核销单品信息
 */
@Data
public class GoodsDetail {
	
	/**
	 * 商品编码
	 */
	private String goods_id;
	
	/**
	 * 商品备注,goods_remark为备注字段，按照配置原样返回，字段内容在微信后台配置券时进行设置。	1001
	 */
	private String goods_remark;
	
	/**
	 * 单品的总优惠金额，单位为：分	100
	 */
	private Integer discount_amount;
	
	/**
	 * 商品数量,用户购买的数量
	 */
	private Integer quantity;
	
	/**
	 * 商品价格,商品单价，单位为：分。	528800
	 */
	private Integer price;
}
