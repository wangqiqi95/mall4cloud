package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 会员等级表
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevel extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long userLevelId;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 等级类型 0:普通会员 1:付费会员
     */
    private Integer levelType;

    /**
     * 所需成长值
     */
    private Integer needGrowth;

    /**
     * 1:已更新 0:等待更新(等级修改后，用户等级的更新)
     */
    private Integer updateStatus;

	/**
	 * 付费会员，是否可以招募会员；1可以招募，0停止招募
	 */
	private Integer recruitStatus;

	public Long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Long userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getLevelType() {
		return levelType;
	}

	public void setLevelType(Integer levelType) {
		this.levelType = levelType;
	}

	public Integer getNeedGrowth() {
		return needGrowth;
	}

	public void setNeedGrowth(Integer needGrowth) {
		this.needGrowth = needGrowth;
	}

	public Integer getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(Integer updateStatus) {
		this.updateStatus = updateStatus;
	}

	public Integer getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(Integer recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	@Override
	public String toString() {
		return "UserLevel{" +
				"userLevelId=" + userLevelId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",level=" + level +
				",levelName=" + levelName +
				",levelType=" + levelType +
				",needGrowth=" + needGrowth +
				",updateStatus=" + updateStatus +
				",recruitStatus=" + recruitStatus +
				'}';
	}
}
