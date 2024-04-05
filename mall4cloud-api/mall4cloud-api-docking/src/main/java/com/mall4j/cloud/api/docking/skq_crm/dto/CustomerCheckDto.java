package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：会员注册验证接口请求参数
 *
 * @date 2022/1/23 14:54：30
 */
public class CustomerCheckDto implements Serializable {

	private static final long serialVersionUID = 486978161629618695L;
	@ApiModelProperty(value = "手机号，2选1")
	private String mobilephone;

	@ApiModelProperty(value = "unionid，2选1")
	private String union_id;

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	@Override
	public String toString() {
		return "CustomerCheckDto{" + "mobilephone='" + mobilephone + '\'' + ", union_id='" + union_id + '\'' + '}';
	}
}
