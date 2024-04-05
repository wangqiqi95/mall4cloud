
package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.common.util.PrincipalUtil;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author lhd
 * @date 2020/12/30
 */
public class SendAndCheckSmsDTO {

	@ApiModelProperty(value = "手机号")
	@Pattern(regexp= PrincipalUtil.MOBILE_REGEXP,message = "请输入正确的手机号")
	@NotBlank(message = "手机号不能为空")
	private String mobile;

	@ApiModelProperty(value = "是否店铺账号，1是")
	private Integer shopAccount;

	@ApiModelProperty(value = "验证码")
	@Pattern(regexp= PrincipalUtil.VERIFICATION_CODE,message = "请输入正确的验证码")
	private String validCode;

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getShopAccount() {
		return shopAccount;
	}

	public void setShopAccount(Integer shopAccount) {
		this.shopAccount = shopAccount;
	}

	@Override
	public String toString() {
		return "SendAndCheckSmsDTO{" +
				"mobile='" + mobile + '\'' +
				", shopAccount=" + shopAccount +
				", validCode='" + validCode + '\'' +
				'}';
	}
}
