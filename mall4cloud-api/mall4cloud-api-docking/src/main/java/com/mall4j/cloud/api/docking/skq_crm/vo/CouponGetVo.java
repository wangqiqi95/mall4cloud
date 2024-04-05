package com.mall4j.cloud.api.docking.skq_crm.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：优惠券查询接口响应内容
 *
 * @date 2022/1/24 17:10：56
 */
public class CouponGetVo implements Serializable {

	private static final long serialVersionUID = 3656857580560418986L;
	@ApiModelProperty(value = "CRM优惠券项目id")
	private String coupon_rule_id;

	@ApiModelProperty(value = "小程序优惠券项目id")
	private String wechat_coupon_rule_id;

	@ApiModelProperty(value = "优惠券码")
	private String coupon_code;

	@ApiModelProperty(value = "券状态，VALID:可使用，EXPIRED：已过期，USED：已使用")
	private String status;

	@ApiModelProperty(value = "优惠券名称")
	private String coupon_name;

	@ApiModelProperty(value = "优惠券类型，MEMBERSHIP:会员优惠券, WELFARE:员工福利券, PAPER: 纸质券")
	private String type;

	@ApiModelProperty(value = "使用效果，DISCOUNT:折扣,CASH_COUPON:代金 ")
	private String use_effect;

	@ApiModelProperty(value = "减免金额，例如：50")
	private BigDecimal money;

	@ApiModelProperty(value = "券折扣,例如：8.8")
	private BigDecimal discount;

	@ApiModelProperty(value = "券有效期开始时间,例如：2018-01-01 12:00：00")
	private String begin_time;

	@ApiModelProperty(value = "券有效期结束时间,例如：2018-01-01 12:00：00")
	private String end_time;

	@ApiModelProperty(value = "优惠券使用须知")
	private String coupon_rule_detail;

	@ApiModelProperty(value = "商品原价金额不小于")
	private BigDecimal original_price;

	@ApiModelProperty(value = "是否以正价为前提")
	private String is_price;

	@ApiModelProperty(value = "是否排除促销策略")
	private String is_promotion;

	@ApiModelProperty(value = "优惠券副标题")
	private String subheading;

	@ApiModelProperty(value = "商品最大数量限制")
	private Integer product_count_limit;

	public String getCoupon_rule_id() {
		return coupon_rule_id;
	}

	public void setCoupon_rule_id(String coupon_rule_id) {
		this.coupon_rule_id = coupon_rule_id;
	}

	public String getWechat_coupon_rule_id() {
		return wechat_coupon_rule_id;
	}

	public void setWechat_coupon_rule_id(String wechat_coupon_rule_id) {
		this.wechat_coupon_rule_id = wechat_coupon_rule_id;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCoupon_name() {
		return coupon_name;
	}

	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUse_effect() {
		return use_effect;
	}

	public void setUse_effect(String use_effect) {
		this.use_effect = use_effect;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getCoupon_rule_detail() {
		return coupon_rule_detail;
	}

	public void setCoupon_rule_detail(String coupon_rule_detail) {
		this.coupon_rule_detail = coupon_rule_detail;
	}

	public BigDecimal getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(BigDecimal original_price) {
		this.original_price = original_price;
	}

	public String getIs_price() {
		return is_price;
	}

	public void setIs_price(String is_price) {
		this.is_price = is_price;
	}

	public String getIs_promotion() {
		return is_promotion;
	}

	public void setIs_promotion(String is_promotion) {
		this.is_promotion = is_promotion;
	}

	public String getSubheading() {
		return subheading;
	}

	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}

	public Integer getProduct_count_limit() {
		return product_count_limit;
	}

	public void setProduct_count_limit(Integer product_count_limit) {
		this.product_count_limit = product_count_limit;
	}

	@Override
	public String toString() {
		return "CouponGetVo{" + "coupon_rule_id='" + coupon_rule_id + '\'' + ", wechat_coupon_rule_id='" + wechat_coupon_rule_id + '\'' + ", coupon_code='"
				+ coupon_code + '\'' + ", status='" + status + '\'' + ", coupon_name='" + coupon_name + '\'' + ", type='" + type + '\'' + ", use_effect='"
				+ use_effect + '\'' + ", money=" + money + ", discount=" + discount + ", begin_time='" + begin_time + '\'' + ", end_time='" + end_time + '\''
				+ ", coupon_rule_detail='" + coupon_rule_detail + '\'' + ", original_price=" + original_price + ", is_price='" + is_price + '\''
				+ ", is_promotion='" + is_promotion + '\'' + ", subheading='" + subheading + '\'' + ", product_count_limit=" + product_count_limit + '}';
	}
}
