package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
public class DistributionCommissionAccount extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 身份类型 1导购 2威客
     */
    private Integer identityType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 工号/卡号
     */
    private String userNumber;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 待结算佣金
     */
    private Long waitCommission;

	/**
	 * 可提现佣金
	 */
	private Long canWithdraw;

	/**
	 * 已提现佣金
	 */
	private Long alreadyWithdraw;

	/**
	 * 提现需退还佣金
	 */
	private Long withdrawNeedRefund;

	/**
	 * 累计提现佣金
	 */
	private Long totalWithdraw;

	/**
	 * 状态 0禁用 1启用
	 */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getWaitCommission() {
		return waitCommission;
	}

	public void setWaitCommission(Long waitCommission) {
		this.waitCommission = waitCommission;
	}

	public Long getCanWithdraw() {
		return canWithdraw;
	}

	public void setCanWithdraw(Long canWithdraw) {
		this.canWithdraw = canWithdraw;
	}

	public Long getAlreadyWithdraw() {
		return alreadyWithdraw;
	}

	public void setAlreadyWithdraw(Long alreadyWithdraw) {
		this.alreadyWithdraw = alreadyWithdraw;
	}

	public Long getTotalWithdraw() {
		return totalWithdraw;
	}

	public void setTotalWithdraw(Long totalWithdraw) {
		this.totalWithdraw = totalWithdraw;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getWithdrawNeedRefund() {
		return withdrawNeedRefund;
	}

	public void setWithdrawNeedRefund(Long withdrawNeedRefund) {
		this.withdrawNeedRefund = withdrawNeedRefund;
	}

	@Override
	public String toString() {
		return "DistributionCommissionAccount{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", id=" + id +
				", identityType=" + identityType +
				", userId=" + userId +
				", username='" + username + '\'' +
				", userNumber='" + userNumber + '\'' +
				", mobile='" + mobile + '\'' +
				", storeId=" + storeId +
				", waitCommission=" + waitCommission +
				", canWithdraw=" + canWithdraw +
				", alreadyWithdraw=" + alreadyWithdraw +
				", withdrawNeedRefund=" + withdrawNeedRefund +
				", totalWithdraw=" + totalWithdraw +
				", status=" + status +
				'}';
	}
}
