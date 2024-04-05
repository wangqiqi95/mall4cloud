package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：单个优惠券码查询接口
 *
 * @date 2023/4/19 13:53：26
 */
@Data
public class CouponSingleGetResp implements Serializable {
	
	private static final long serialVersionUID = 8739146331505052602L;
	
	@ApiModelProperty(value = "CRM优惠券项目id")
	private String coupon_rule_id;
	
	@ApiModelProperty(value = "小程序优惠券项目id")
	private String wechat_coupon_rule_id;
	
	@ApiModelProperty(value = "优惠券码")
	private String coupon_code;
	
	@ApiModelProperty(value = "会员卡号")
	private String vipcode;
	
	@ApiModelProperty(value = "券状态(VALID: 有效, USED: 已使用 ,EXPIRED: 过期)")
	private String status;
	
	@ApiModelProperty(value = "优惠券名称")
	private String coupon_name;
	
	@ApiModelProperty(value = "优惠券类型(MEMBERSHIP:会员优惠券, WELFARE:员工福利券, PAPER: 纸质券)")
	private String type;
	
	@ApiModelProperty(value = "使用效果(DISCOUNT:折扣,CASH_COUPON:代金)")
	private String use_effect;
	
	@ApiModelProperty(value = "减免金额,显示优惠券的优惠金额")
	private BigDecimal money;
	
	@ApiModelProperty(value = "折扣券显示的折扣金额")
	private BigDecimal discount;
	
	@ApiModelProperty(value = "优惠券的创建有效时间")
	private String begin_time;
	
	@ApiModelProperty(value = "优惠券的有效结束时间")
	private String end_time;
	
	@ApiModelProperty(value = "折后限额")
	private BigDecimal account_limit;
	
	@ApiModelProperty(value = "优惠券使用须知")
	private String coupon_rule_detail;
	
	@ApiModelProperty(value = "商品原价金额不能小于")
	private BigDecimal original_price;
	
	@ApiModelProperty(value = "是否以正价为前提(Y/N)")
	private String is_price;
	
	@ApiModelProperty(value = "是否排除促销策略(Y/N)")
	private String is_promotion;
	
	@ApiModelProperty(value = "付款方式是否共用(Y/N)")
	private String isshare_paytype;
	
	@ApiModelProperty(value = "同类型是否多张使用(Y/N)")
	private String isshare_doutype;
	
	@ApiModelProperty(value = "优惠券副标题")
	private String subheading;
	
	@ApiModelProperty(value = "客户姓名")
	private String customer_name;
	
	@ApiModelProperty(value = "门店限制类型(NO_LIMIT:不限门店，LIMIT_CHANNEL：指定门店)")
	private String store_condition_type;
	
	@ApiModelProperty(value = "门店列表(指定门店则不为空，其他则为空)")
	private List<CouponStoreResultResp> store_result;

}
