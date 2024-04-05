package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：优惠券验证接口，参数
 *
 * @date 2022/1/26 9:22：57
 */
public class CouponCheckDto implements Serializable {

	@ApiModelProperty(value = "CRM会员卡号", required = true)
	private String vipcode;

	@ApiModelProperty(value = "来源,wechat", required = true)
	private String source;

	@ApiModelProperty(value = "优惠券码(多个)", required = true)
	private List<String> coupon_ids;

	@ApiModelProperty(value = "门店编码", required = true)
	private String store_code;

	@ApiModelProperty(value = "商品sku", required = true)
	private List<String> sku_codes;

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<String> getCoupon_ids() {
		return coupon_ids;
	}

	public void setCoupon_ids(List<String> coupon_ids) {
		this.coupon_ids = coupon_ids;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public List<String> getSku_codes() {
		return sku_codes;
	}

	public void setSku_codes(List<String> sku_codes) {
		this.sku_codes = sku_codes;
	}

	@Override
	public String toString() {
		return "CouponCheckDto{" + "vipcode='" + vipcode + '\'' + ", source='" + source + '\'' + ", coupon_ids=" + coupon_ids + ", store_code='" + store_code
				+ '\'' + ", sku_codes=" + sku_codes + '}';
	}
}
