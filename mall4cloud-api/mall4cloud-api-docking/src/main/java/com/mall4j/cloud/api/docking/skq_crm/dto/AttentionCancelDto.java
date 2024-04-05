package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：会员关注、取关接口,请求参数
 *
 * @date 2022/1/26 10:49：56
 */
public class AttentionCancelDto implements Serializable {

	private static final long serialVersionUID = -5273747467770795117L;
	@ApiModelProperty(value = "CRM会员卡号", required = true)
	private String vipcode;

	@ApiModelProperty(value = "成人公众号关注状态,2选1,1关注，2取关")
	private Integer adult_status;

	@ApiModelProperty(value = "儿童公众号关注状态,2选1,1关注，2取关")
	private Integer child_status;

	@ApiModelProperty(value = "是否关注lifestyle公众号,1关注，2取关")
	private Integer follow_lifestyle;

	@ApiModelProperty(value = "是否关注sport公众号,1关注，2取关")
	private Integer follow_sport;

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public Integer getAdult_status() {
		return adult_status;
	}

	public void setAdult_status(Integer adult_status) {
		this.adult_status = adult_status;
	}

	public Integer getChild_status() {
		return child_status;
	}

	public void setChild_status(Integer child_status) {
		this.child_status = child_status;
	}

	public Integer getFollow_lifestyle() {
		return follow_lifestyle;
	}

	public void setFollow_lifestyle(Integer follow_lifestyle) {
		this.follow_lifestyle = follow_lifestyle;
	}

	public Integer getFollow_sport() {
		return follow_sport;
	}

	public void setFollow_sport(Integer follow_sport) {
		this.follow_sport = follow_sport;
	}

	@Override
	public String toString() {
		return "AttentionCancelDto{" + "vipcode='" + vipcode + '\'' + ", adult_status=" + adult_status + ", child_status=" + child_status
				+ ", follow_lifestyle=" + follow_lifestyle + ", follow_sport=" + follow_sport + '}';
	}
}
