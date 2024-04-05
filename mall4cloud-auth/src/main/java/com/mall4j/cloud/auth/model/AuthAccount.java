package com.mall4j.cloud.auth.model;

import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.annotations.DistributedId;
import com.mall4j.cloud.common.model.BaseModel;

/**
 * 统一账户信息
 *
 * @author FrozenWatermelon
 * @date 2020/07/02
 */
public class AuthAccount extends BaseModel {

	/**
	 * 全平台用户唯一id
	 */
	@DistributedId(DistributedIdKey.MALL4CLOUD_AUTH_USER)
	private Long uid;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建ip
	 */
	private String createIp;

	/**
	 * 状态 1:启用 0:禁用 -1:删除
	 */
	private Integer status;

	/**
	 * 系统类型见SysTypeEnum 0.普通用户系统 1.商家端
	 */
	private Integer sysType;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 所属租户
	 */
	private Long tenantId;

	/**
	 * 是否是管理员
	 */
	private Integer isAdmin;

	/**
	 * 店铺是否已经入驻 0: 未入驻，1：已入驻
	 */
	private Integer isPassShop;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getIsPassShop() {
		return isPassShop;
	}

	public void setIsPassShop(Integer isPassShop) {
		this.isPassShop = isPassShop;
	}

	@Override
	public String toString() {
		return "AuthAccount{" +
				"uid=" + uid +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", createIp='" + createIp + '\'' +
				", status=" + status +
				", sysType=" + sysType +
				", userId=" + userId +
				", tenantId=" + tenantId +
				", isAdmin=" + isAdmin +
				", isPassShop=" + isPassShop +
				'}';
	}

}
