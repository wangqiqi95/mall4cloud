package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户提货人信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
public class UserConsigneeDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long userConsigneeId;

    @ApiModelProperty("提货人姓名")
    private String name;

    @ApiModelProperty("提货人联系方式")
    private String mobile;

	public Long getUserConsigneeId() {
		return userConsigneeId;
	}

	public void setUserConsigneeId(Long userConsigneeId) {
		this.userConsigneeId = userConsigneeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "UserConsigneeDTO{" +
				"userConsigneeId=" + userConsigneeId +
				",name=" + name +
				",mobile=" + mobile +
				'}';
	}
}
