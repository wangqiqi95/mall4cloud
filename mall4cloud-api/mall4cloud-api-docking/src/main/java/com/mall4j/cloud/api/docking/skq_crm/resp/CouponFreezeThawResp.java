package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CouponFreezeThawResp implements Serializable {

	@ApiModelProperty(value = "冻结时返回")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "CouponFreezeThawResp{" + "token='" + token + '\'' + '}';
	}
}
