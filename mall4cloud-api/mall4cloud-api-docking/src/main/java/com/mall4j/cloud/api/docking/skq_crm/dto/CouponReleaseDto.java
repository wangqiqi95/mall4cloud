package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：优惠券发放（小程序—>CRM）请求参数
 *
 * @date 2022/1/26 9:10：17
 */
public class CouponReleaseDto implements Serializable {

	private static final long serialVersionUID = -8778732443543177664L;
	@ApiModelProperty(value = "CRM卡号，3选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id，3选1")
	private String union_id;

	@ApiModelProperty(value = "手机号，3选1")
	private String mobile;

	@ApiModelProperty(value = "小程序优惠券项目id", required = true)
	private String wechat_coupon_rule_id;

	@ApiModelProperty(value = "消耗积分值", required = true)
	private Integer points;

	@ApiModelProperty(value = "请求id", required = true)
	private String request_id;

	@ApiModelProperty(value = "场景值")
	private String scene;

	@ApiModelProperty(value = "店铺code,会根据ERP门店进行校验是否存在，如果校验失败则返回失败")
	private String store_no;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWechat_coupon_rule_id() {
		return wechat_coupon_rule_id;
	}

	public void setWechat_coupon_rule_id(String wechat_coupon_rule_id) {
		this.wechat_coupon_rule_id = wechat_coupon_rule_id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getStore_no() {
		return store_no;
	}

	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}

	@Override
	public String toString() {
		return "CouponReleaseDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", mobile='" + mobile + '\''
				+ ", wechat_coupon_rule_id='" + wechat_coupon_rule_id + '\'' + ", points='" + points + '\'' + ", request_id='" + request_id + '\'' + ", scene='"
				+ scene + '\'' + ", store_no='" + store_no + '\'' + '}';
	}
}
