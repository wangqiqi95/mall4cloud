package com.mall4j.cloud.user.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户提货人信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
public class UserConsignee extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long userConsigneeId;

	/**
	 * id
	 */
	private Long userId;
    /**
     * 提货人姓名
     */
    private String name;

    /**
     * 提货人联系方式
     */
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserConsignee{" +
				"userConsigneeId=" + userConsigneeId +
				", userId=" + userId +
				", name='" + name + '\'' +
				", mobile='" + mobile + '\'' +
				'}';
	}
}
