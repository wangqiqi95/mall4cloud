package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：会员基本信息查询请求参数
 *
 * @date 2022/1/22 17:35：56
 */
public class CustomerGetDto implements Serializable {

	private static final long serialVersionUID = 5666200162438730699L;
	@ApiModelProperty(value = "CRM卡号,3选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id,3选1")
	private String union_id;

	@ApiModelProperty(value = "手机号,3选1")
	private String mobile;

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

	@Override
	public String toString() {
		return "CustomerGetDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", mobile='" + mobile + '\'' + '}';
	}

	public Map<String, Object> toMap() {
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", this.mobile);
		params.put("union_id", this.union_id);
		params.put("vipcode", this.vipcode);
		return params;
	}
}
