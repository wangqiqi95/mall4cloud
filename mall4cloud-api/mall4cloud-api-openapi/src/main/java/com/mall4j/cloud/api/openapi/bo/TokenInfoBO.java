package com.mall4j.cloud.api.openapi.bo;

/**
 * token信息，该信息存在redis中
 *
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public class TokenInfoBO<T> {

	/**
	 * 保存在token信息里面的用户信息
	 */
	private T userInfoInToken;

	private String accessToken;

	private String refreshToken;

	/**
	 * 在多少秒后过期
	 */
	private Integer expiresIn;

	private String scope;

	private String tokentype;

	public T getUserInfoInToken() {
		return userInfoInToken;
	}

	public void setUserInfoInToken(T t) {
		this.userInfoInToken = t;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTokentype() {
		return tokentype;
	}

	public void setTokentype(String tokentype) {
		this.tokentype = tokentype;
	}

	@Override public String toString() {
		return "TokenInfoBO{" + "userInfoInToken=" + userInfoInToken + ", accessToken='" + accessToken + '\'' + ", refreshToken='" + refreshToken + '\''
				+ ", expiresIn=" + expiresIn + ", scope='" + scope + '\'' + ", tokentype='" + tokentype + '\'' + '}';
	}
}
