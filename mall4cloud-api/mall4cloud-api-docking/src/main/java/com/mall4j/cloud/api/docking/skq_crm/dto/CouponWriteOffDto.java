package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：优惠券核销请求参数
 *
 * @date 2022/1/26 10:15：39
 */
public class CouponWriteOffDto implements Serializable {

	private static final long serialVersionUID = 4941685233079373888L;
	@ApiModelProperty(value = "CRM卡号，2选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id，2选1")
	private String union_id;

	@ApiModelProperty(value = "券码", required = true)
	private String coupon_code;

	@ApiModelProperty(value = "门店编码", required = true)
	private String store_code;

	@ApiModelProperty(value = "销售单号", required = true)
	private String order_no;

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@Override
	public String toString() {
		return "CouponWriteOffDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", coupon_code='" + coupon_code + '\''
				+ ", store_code='" + store_code + '\'' + ", order_no='" + order_no + '\'' + '}';
	}
}
