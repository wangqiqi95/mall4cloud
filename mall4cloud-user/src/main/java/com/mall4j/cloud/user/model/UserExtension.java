package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserExtension extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long userExtensionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;

    /**
     * 用户当前成长值
     */
    private Integer growth;

    /**
     * 用户积分
     */
    private Long score;

    /**
     * 用户总余额
     */
    private Long balance;

    /**
     * 用户实际余额
     */
    private Long actualBalance;

    /**
     * 乐观锁
     */
    private Integer version;

	/**
	 * 连续签到天数
	 */
	private Integer signDay;

	/**
	 * 用户付费会员等级
	 */
	private Integer vipLevel;

	/**
	 * 最近消费时间
	 */
	private Date recentConsumerTime;

	/**
	 * 最近回访时间
	 */
	private Date recentVisitTime;

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

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Date getRecentConsumerTime() {
		return recentConsumerTime;
	}

	public void setRecentConsumerTime(Date recentConsumerTime) {
		this.recentConsumerTime = recentConsumerTime;
	}

	public Date getRecentVisitTime() {
		return recentVisitTime;
	}

	public void setRecentVisitTime(Date recentVisitTime) {
		this.recentVisitTime = recentVisitTime;
	}

	@Override
	public String toString() {
		return "UserExtension{" +
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
				", vipLevel=" + vipLevel +
				'}';
	}
}
