package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 用户等级记录DTO
 *
 * @author FrozenWatermelon
 * @date 2021-05-14 11:04:52
 */
public class UserLevelLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级记录表")
    private Long levelLogId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("变更类型 -1降级 0不变 1升级")
    private Integer levelIoType;

    @ApiModelProperty("等级变更原因1.成长值不足 2.成长值足够 3.购买会员 4.续费会员 5.会员到期")
    private Integer levelChangeReason;

    @ApiModelProperty("变更之前等级")
    private Integer beforeLevel;

    @ApiModelProperty("变更之前的等级类型  0:普通会员 1:付费会员")
    private Integer beforeLevelType;

    @ApiModelProperty("变更之后的等级")
    private Integer afterLevel;

    @ApiModelProperty("变更之后的等级类型 0:普通会员 1:付费会员")
    private Integer afterLevelType;

    @ApiModelProperty("支付单号")
    private Long payId;

    @ApiModelProperty("是否支付1已支付0未支付")
    private Integer isPayed;

    @ApiModelProperty("支付金额")
    private Long payAmount;

	public Long getLevelLogId() {
		return levelLogId;
	}

	public void setLevelLogId(Long levelLogId) {
		this.levelLogId = levelLogId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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
		return "UserLevelLogDTO{" +
				"levelLogId=" + levelLogId +
				",userId=" + userId +
				",levelIoType=" + levelIoType +
				",levelChangeReason=" + levelChangeReason +
				",beforeLevel=" + beforeLevel +
				",beforeLevelType=" + beforeLevelType +
				",afterLevel=" + afterLevel +
				",afterLevelType=" + afterLevelType +
				",payId=" + payId +
				",isPayed=" + isPayed +
				",payAmount=" + payAmount +
				'}';
	}
}
