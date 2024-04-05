package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserBalanceLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long balanceLogId;

    /**
     * 用户id
     */
    private Long userId;

	/**
	 * 充值id
	 */
	private Long rechargeId;

    /**
     * 改变金额
     */
    private Long changeBalance;

    /**
     * 收支类型 0支出 1收入
     */
    private Integer ioType;

    /**
     * 支付编号(用于余额充值)
     */
    private Long payId;

    /**
     * 1:充值 2:赠送 3:支付  4:退款 5:平台手动修改
	 * RechargeTypeEnum
     */
    private Integer type;

    /**
     * 1:已支付 0:未支付(用于余额充值)
     */
    private Integer isPayed;

	/**
	 * 退款单号(用于余额支付进行退款)
	 */
	private Long refundId;

	public Long getBalanceLogId() {
		return balanceLogId;
	}

	public void setBalanceLogId(Long balanceLogId) {
		this.balanceLogId = balanceLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getChangeBalance() {
		return changeBalance;
	}

	public void setChangeBalance(Long changeBalance) {
		this.changeBalance = changeBalance;
	}

	public Integer getIoType() {
		return ioType;
	}

	public void setIoType(Integer ioType) {
		this.ioType = ioType;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	public Long getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Long rechargeId) {
		this.rechargeId = rechargeId;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	@Override
	public String toString() {
		return "UserBalanceLog{" +
				"balanceLogId=" + balanceLogId +
				", userId=" + userId +
				", rechargeId=" + rechargeId +
				", changeBalance=" + changeBalance +
				", ioType=" + ioType +
				", payId=" + payId +
				", type=" + type +
				", isPayed=" + isPayed +
				", refundId=" + refundId +
				"} " + super.toString();
	}
}
