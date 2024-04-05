package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 订单结算表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public class OrderSettlement extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 支付结算单据ID
     */
    private Long settlementId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 是否清算 0:否 1:是
     */
    private Integer isClearing;

    /**
     * 支付积分
     */
    private Long payScore;

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 清算时间
     */
    private Date clearingTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否已支付，1.已支付0.未支付
     */
    private Integer isPayed;

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getIsClearing() {
		return isClearing;
	}

	public void setIsClearing(Integer isClearing) {
		this.isClearing = isClearing;
	}

	public Long getPayScore() {
		return payScore;
	}

	public void setPayScore(Long payScore) {
		this.payScore = payScore;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public Date getClearingTime() {
		return clearingTime;
	}

	public void setClearingTime(Date clearingTime) {
		this.clearingTime = clearingTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	@Override
	public String toString() {
		return "OrderSettlement{" +
				"settlementId=" + settlementId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",orderId=" + orderId +
				",payId=" + payId +
				",payType=" + payType +
				",isClearing=" + isClearing +
				",payScore=" + payScore +
				",payAmount=" + payAmount +
				",clearingTime=" + clearingTime +
				",version=" + version +
				",isPayed=" + isPayed +
				'}';
	}
}
