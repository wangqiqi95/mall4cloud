package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户扩展信息DTO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserExtensionDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty()
    private Long userExtensionId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户等级")
    private Integer level;

    @ApiModelProperty("等级条件 0 普通会员 1 付费会员")
    private Integer levelType;

    @ApiModelProperty("用户当前成长值")
    private Integer growth;

    @ApiModelProperty("用户积分")
    private Long score;

    @ApiModelProperty("用户总余额")
    private Long balance;

    @ApiModelProperty("用户实际余额")
    private Long actualBalance;

    @ApiModelProperty("乐观锁")
    private Integer version;

	@ApiModelProperty("连续签到天数")
	private Integer signDay;

	@ApiModelProperty("改变余额数值")
	private Long changeBalance;

	@ApiModelProperty("更新时间")
	protected Date updateTime;


	public Integer getSignDay() {
		return signDay;
	}

	public void setSignDay(Integer signDay) {
		this.signDay = signDay;
	}

	public Long getUserExtensionId() {
		return userExtensionId;
	}

	public void setUserExtensionId(Long userExtensionId) {
		this.userExtensionId = userExtensionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevelType() {
		return levelType;
	}

	public void setLevelType(Integer levelType) {
		this.levelType = levelType;
	}

	public Integer getGrowth() {
		return growth;
	}

	public void setGrowth(Integer growth) {
		this.growth = growth;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getActualBalance() {
		return actualBalance;
	}

	public void setActualBalance(Long actualBalance) {
		this.actualBalance = actualBalance;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getChangeBalance() {
		return changeBalance;
	}

	public void setChangeBalance(Long changeBalance) {
		this.changeBalance = changeBalance;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "UserExtensionDTO{" +
				"userExtensionId=" + userExtensionId +
				", userId=" + userId +
				", level=" + level +
				", levelType=" + levelType +
				", growth=" + growth +
				", score=" + score +
				", balance=" + balance +
				", actualBalance=" + actualBalance +
				", version=" + version +
				", signDay=" + signDay +
				", changeBalance=" + changeBalance +
				", updateTime=" + updateTime +
				'}';
	}
}
