package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：会员注册验证接口响应
 *
 * @date 2022/1/23 14:56：58
 */
public class CustomerCheckResp implements Serializable {

	private static final long serialVersionUID = -1416948896902493863L;
	@ApiModelProperty(value = "是否存在,Y/N")
	private String is_exists;

	@ApiModelProperty(value = "会员卡号")
	private String vipcode;

	public String getIs_exists() {
		return is_exists;
	}

	public void setIs_exists(String is_exists) {
		this.is_exists = is_exists;
	}

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	@Override
	public String toString() {
		return "CustomerCheckResp{" + "is_exists='" + is_exists + '\'' + ", vipcode='" + vipcode + '\'' + '}';
	}
}
