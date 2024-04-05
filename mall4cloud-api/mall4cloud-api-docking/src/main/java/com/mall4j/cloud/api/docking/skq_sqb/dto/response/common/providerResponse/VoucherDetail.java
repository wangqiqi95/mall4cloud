package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.providerResponse;

import lombok.Data;

import java.util.List;

/**
 * 核销券信息
 */
@Data
public class VoucherDetail {
	
	/**
	 * 券ID,券或者立减优惠id	109519
	 */
	private String promotionId;
	
	/**
	 * 优惠名称	单品惠-6
	 */
	private String name;
	
	/**
	 * 优惠范围,GLOBAL- 全场代金券,SINGLE- 单品优惠	SINGLE
	 */
	private String scope;
	
	/**
	 * 优惠类型,COUPON- 代金券，需要走结算资金的充值型代金券,（境外商户券币种与支付币种一致）
	 * 		   DISCOUNT- 优惠券，不走结算资金的免充值型优惠券，（境外商户券币种与标价币种一致)	DISCOUNT
	 */
	private String type;
	
	/**
	 * 必传,优惠券面额,用户享受优惠的金额，单位为：分	5
	 */
	private String amount;
	
	/**
	 * 必传,活动ID,在微信商户后台配置的批次ID	931386
	 */
	private String voucherId;
	
	/**
	 * 微信出资,特指由微信支付商户平台创建的优惠，出资金额等于本项优惠总金额，单位为分	0
	 */
	private String wxpayContribute;
	
	/**
	 * 商户出资,特指商户自己创建的优惠，出资金额等于本项优惠总金额，单位为分	0
	 */
	private String merchantContribute;
	
	/**
	 * 其他出资方出资金额，单位为分	5
	 */
	private String otherContribute;
	
	/**
	 * 单品列表	[]	否	单品信息，使用Json格式
	 */
	private List<GoodsDetail> goods_detail;
}
