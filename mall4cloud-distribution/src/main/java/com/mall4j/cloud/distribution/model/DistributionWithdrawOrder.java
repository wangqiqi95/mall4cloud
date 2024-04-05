package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public class DistributionWithdrawOrder extends BaseModel implements Serializable{
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
     * 提现记录ID
     */
    private Long recordId;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 订单ID
	 */
	private Long orderId;

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

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawOrder{" +
				"id=" + id +
				", identityType=" + identityType +
				", recordId=" + recordId +
				", userId=" + userId +
				", orderId=" + orderId +
				'}';
	}
}
