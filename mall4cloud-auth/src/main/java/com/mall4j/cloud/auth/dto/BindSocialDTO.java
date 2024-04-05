package com.mall4j.cloud.auth.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 用于登陆传递账号密码
 *
 * @author FrozenWatermelon
 * @date 2021/01/19
 */
public class BindSocialDTO {

	@NotBlank
	@ApiModelProperty(value = "尝试社交登录的时候返回的tempUid", required = true)
	private String tempUid;

	@NotBlank
	@ApiModelProperty(value = "校验账号，邮箱 or 手机号", required = true)
	private String validAccount;

	@ApiModelProperty(value = "验证码")
	private String validCode;

	public String getTempUid() {
		return tempUid;
	}

	public void setTempUid(String tempUid) {
		this.tempUid = tempUid;
	}

	public String getValidAccount() {
		return validAccount;
	}

	public void setValidAccount(String validAccount) {
		this.validAccount = validAccount;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	@Override
	public String toString() {
		return "BindSocialDTO{" +
				"tempUid='" + tempUid + '\'' +
				", validAccount='" + validAccount + '\'' +
				", validCode='" + validCode + '\'' +
				'}';
	}
}
