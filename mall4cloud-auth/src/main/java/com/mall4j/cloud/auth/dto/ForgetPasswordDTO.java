package com.mall4j.cloud.auth.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 忘记密码
 *
 * @author FrozenWatermelon
 * @date 2021/01/29
 */
public class ForgetPasswordDTO {

	@NotBlank
	@ApiModelProperty(value = "校验账号，邮箱 or 手机号", required = true)
	private String validAccount;

	@NotBlank
	@ApiModelProperty(value = "验证码", required = true)
	private String validateCode;

	@NotNull(message = "newPassword NotNull")
	@ApiModelProperty(value = "新密码", required = true)
	private String newPassword;

	@NotNull
	@ApiModelProperty(value = "系统类型 0普通用户 1商家端 2平台端", required = true)
	private Integer sysType;

	public String getValidAccount() {
		return validAccount;
	}

	public void setValidAccount(String validAccount) {
		this.validAccount = validAccount;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	@Override
	public String toString() {
		return "ForgetPasswordDTO{" +
				"validAccount='" + validAccount + '\'' +
				", validateCode='" + validateCode + '\'' +
				", newPassword='" + newPassword + '\'' +
				", sysType=" + sysType +
				'}';
	}
}
