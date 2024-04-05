package com.mall4j.cloud.api.auth.bo;

/**
 * 保存在token信息里面的用户信息
 *
 * com.mall4j.cloud.auth.service.impl.AuthAccountServiceImpl
 * com.mall4j.cloud.auth.controller.LoginController
 * @author FrozenWatermelon
 * @date 2020/7/3
 */
public class UserInfoInTokenBO {

	/**
	 * 用户在自己系统的用户id
	 */
	private Long userId;

	/**
	 * 用户在自己系统的用户名称
	 */
	private String username;

	/**
	 * 全局唯一的id,
	 */
	private Long uid;

	/**
	 * 租户id (商家id)
	 */
	private Long tenantId;

	/**
	 * 系统类型
	 * @see com.mall4j.cloud.api.auth.constant.SysTypeEnum
	 */
	private Integer sysType;

	/**
	 * 是否是管理员
	 */
	private Integer isAdmin;

	/**
	 * 店铺是否已经入驻 0: 未入驻，1：已入驻
	 */
	private Integer isPassShop;

	private String bizUserId;

	private String bizUid;

	/**
	 * 第三方系统类型
	 */
	private Integer socialType;

	/**
	 * 小程序session_key
	 */
	private String sessionKey;

	/**
	 * 所属门店ID
	 */
	private Long storeId;

	/**
	 * orgId
	 */
	private Long orgId;


	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Integer getSocialType() {
		return socialType;
	}

	public void setSocialType(Integer socialType) {
		this.socialType = socialType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getBizUserId() {
		return bizUserId;
	}

	public void setBizUserId(String bizUserId) {
		this.bizUserId = bizUserId;
	}

	public String getBizUid() {
		return bizUid;
	}

	public void setBizUid(String bizUid) {
		this.bizUid = bizUid;
	}

	public Integer getIsPassShop() {
		return isPassShop;
	}

	public void setIsPassShop(Integer isPassShop) {
		this.isPassShop = isPassShop;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserInfoInTokenBO{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", uid=" + uid +
				", tenantId=" + tenantId +
				", sysType=" + sysType +
				", isAdmin=" + isAdmin +
				", isPassShop=" + isPassShop +
				", bizUserId='" + bizUserId + '\'' +
				", bizUid='" + bizUid + '\'' +
				", socialType=" + socialType +
				", sessionKey='" + sessionKey + '\'' +
				", storeId=" + storeId +
				", orgId=" + orgId +
				'}';
	}
}
