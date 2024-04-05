package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户等级记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-14 11:04:52
 */
public class UserLevelLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 等级记录表
     */
    private Long levelLogId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 变更类型 -1降级 0不变 1升级
     */
    private Integer levelIoType;

    /**
     * 等级变更原因1.成长值不足 2.成长值足够 3.购买会员 4.续费会员 5.会员到期
     */
    private Integer levelChangeReason;

    /**
     * 变更之前等级
     */
    private Integer beforeLevel;

    /**
     * 变更之前的等级类型  0:普通会员 1:付费会员
     */
    private Integer beforeLevelType;

    /**
     * 变更之后的等级
     */
    private Integer afterLevel;

    /**
     * 变更之后的等级类型 0:普通会员 1:付费会员
     */
    private Integer afterLevelType;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 是否支付1已支付0未支付
     */
    private Integer isPayed;

    /**
     * 支付金额
     */
    private Long payAmount;

	/**
	 * 期数类型(0:月,1:季,2:年)
	 */
	private Integer termType;

	public Integer getTermType() {
		return termType;
	}

	public void setTermType(Integer termType) {
		this.termType = termType;
	}

	public Long getLevelLogId() {
		return levelLogId;
	}

	public void setLevelLogId(Long levelLogId) {
		this.levelLogId = levelLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getLevelIoType() {
		return levelIoType;
	}

	public void setLevelIoType(Integer levelIoType) {
		this.levelIoType = levelIoType;
	}

	public Integer getLevelChangeReason() {
		return levelChangeReason;
	}

	public void setLevelChangeReason(Integer levelChangeReason) {
		this.levelChangeReason = levelChangeReason;
	}

	public Integer getBeforeLevel() {
		return beforeLevel;
	}

	public void setBeforeLevel(Integer beforeLevel) {
		this.beforeLevel = beforeLevel;
	}

	public Integer getBeforeLevelType() {
		return beforeLevelType;
	}

	public void setBeforeLevelType(Integer beforeLevelType) {
		this.beforeLevelType = beforeLevelType;
	}

	public Integer getAfterLevel() {
		return afterLevel;
	}

	public void setAfterLevel(Integer afterLevel) {
		this.afterLevel = afterLevel;
	}

	public Integer getAfterLevelType() {
		return afterLevelType;
	}

	public void setAfterLevelType(Integer afterLevelType) {
		this.afterLevelType = afterLevelType;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	@Override
	public String toString() {
		return "UserLevelLog{" +
				"levelLogId=" + levelLogId +
				", userId='" + userId + '\'' +
				", levelIoType=" + levelIoType +
				", levelChangeReason=" + levelChangeReason +
				", beforeLevel=" + beforeLevel +
				", beforeLevelType=" + beforeLevelType +
				", afterLevel=" + afterLevel +
				", afterLevelType=" + afterLevelType +
				", payId=" + payId +
				", isPayed=" + isPayed +
				", payAmount=" + payAmount +
				", termType=" + termType +
				"} " + super.toString();
	}
}
