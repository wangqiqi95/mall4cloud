package com.mall4j.cloud.auth.model;

import com.mall4j.cloud.common.model.BaseModel;

/**
 * 登陆日志
 *
 * @author FrozenWatermelon
 * @date 2020/07/02
 */
public class AuthLog extends BaseModel {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 本系统userId
	 */
	private Long userId;

	/**
	 * 登陆应用 1.小程序 2.微信公众号 3.PC 4.H5 5.ANDROID 1.IOS
	 */
	private Integer appType;

	/**
	 * 登录ip
	 */
	private String loginIp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Override
	public String toString() {
		return "AuthLog{" + "id=" + id + ", userId=" + userId + ", appType=" + appType + ", loginIp='" + loginIp + '\''
				+ "} " + super.toString();
	}

}
