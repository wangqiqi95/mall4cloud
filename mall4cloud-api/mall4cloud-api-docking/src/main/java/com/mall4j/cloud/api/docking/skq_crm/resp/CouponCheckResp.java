package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：优惠券验证接口响应
 *
 * @date 2022/1/26 9:29：45
 */
public class CouponCheckResp implements Serializable {

	private static final long serialVersionUID = 3971349235567686060L;
	@ApiModelProperty(value = "优惠券码")
	private String coupon_code;

	@ApiModelProperty(value = "门店是否可用")
	private String store_status;

	@ApiModelProperty(value = "商品是否可用")
	private String sku_status;

	@ApiModelProperty(value = "优惠券名称")
	private String coupon_name;

	@ApiModelProperty(value = "优惠券类型,MEMBERSHIP:会员优惠券, WELFARE:员工福利券, PAPER: 纸质券")
	private String type;

	@ApiModelProperty(value = "使用效果,DISCOUNT:折扣,CASH_COUPON:代金 ")
	private String use_effect;

	@ApiModelProperty(value = "减免金额,100")
	private BigDecimal money;

	@ApiModelProperty(value = "券折扣,8.8")
	private BigDecimal discout;

	@ApiModelProperty(value = "券有效期开始时间,2022-11-08 23:59:59")
	private String begin_time;

	@ApiModelProperty(value = "券有效期结束时间,2022-11-08 23:59:59")
	private String end_time;

	@ApiModelProperty(value = "优惠券使用须知,本券适用门店xxxxxx，本券一次只能使用一张xx")
	private String coupon_rule_detail;

	@ApiModelProperty(value = "商品原价金额不小于")
	private BigDecimal original_price;

	@ApiModelProperty(value = "是否以正价为前提，Y:是；N:否")
	private String is_price;

	@ApiModelProperty(value = "是否排除促销策略，Y:是；N:否")
	private String is_promotion;

	@ApiModelProperty(value = "优惠券副标题")
	private String subheading;

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getStore_status() {
		return store_status;
	}

	public void setStore_status(String store_status) {
		this.store_status = store_status;
	}

	public String getSku_status() {
		return sku_status;
	}

	public void setSku_status(String sku_status) {
		this.sku_status = sku_status;
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

	public BigDecimal getDiscout() {
		return discout;
	}

	public void setDiscout(BigDecimal discout) {
		this.discout = discout;
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

	@Override
	public String toString() {
		return "CouponCheckResp{" + "coupon_code='" + coupon_code + '\'' + ", store_status='" + store_status + '\'' + ", sku_status='" + sku_status + '\''
				+ ", coupon_name='" + coupon_name + '\'' + ", type='" + type + '\'' + ", use_effect='" + use_effect + '\'' + ", money=" + money + ", discout="
				+ discout + ", begin_time='" + begin_time + '\'' + ", end_time='" + end_time + '\'' + ", coupon_rule_detail='" + coupon_rule_detail + '\''
				+ ", original_price=" + original_price + ", is_price='" + is_price + '\'' + ", is_promotion='" + is_promotion + '\'' + ", subheading='"
				+ subheading + '\'' + '}';
	}
}
