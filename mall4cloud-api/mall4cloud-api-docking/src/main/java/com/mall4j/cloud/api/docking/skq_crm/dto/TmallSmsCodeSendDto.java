package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：获取淘宝外域入会验证码请求参数
 *
 * @date 2022/1/26 11:08：25
 */
public class TmallSmsCodeSendDto implements Serializable {

	private static final long serialVersionUID = -6409597759024299292L;
	@ApiModelProperty(value = "手机号", required = true)
	private String mobile;

	@ApiModelProperty(value = "淘宝店铺id")
	private String shopCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	@Override
	public String toString() {
		return "TmallSmsCodeSendDto{" + "mobile='" + mobile + '\'' + ", shopCode='" + shopCode + '\'' + '}';
	}
}
