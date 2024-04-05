package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：会员新增接口响应
 *
 * @date 2022/1/23 15:26：26
 */
public class CustomerAddResp implements Serializable {

	private static final long serialVersionUID = -6233013533796157579L;
	@ApiModelProperty(value = "会员卡号")
	private String vipcode;

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	@Override
	public String toString() {
		return "CustomerAddResp{" + "vipcode='" + vipcode + '\'' + '}';
	}
}
