package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 会员等级表DTO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long userLevelId;

	@NotNull(message = "等级不能为空")
    @ApiModelProperty("等级")
    private Integer level;

	@NotNull(message = "等级名称不能为空")
    @ApiModelProperty("等级名称")
    private String levelName;

	@NotNull(message = "等级类型不能为空")
    @ApiModelProperty("等级类型 0:普通会员 1:付费会员")
    private Integer levelType;

    @ApiModelProperty("所需成长值")
    private Integer needGrowth;

    @ApiModelProperty("1:已更新 0:等待更新(等级修改后，用户等级的更新)")
    private Integer updateStatus;

	@ApiModelProperty("付费会员，是否可以招募会员；1可以招募，0停止招募")
	private Integer recruitStatus;

	@ApiModelProperty("权益id数组")
	private List<Long> userRightsIds;

	@ApiModelProperty("等级期数列表")
	private List<UserLevelTermDTO> userLevelTerms;

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

	public List<Long> getUserRightsIds() {
		return userRightsIds;
	}

	public void setUserRightsIds(List<Long> userRightsIds) {
		this.userRightsIds = userRightsIds;
	}

	public List<UserLevelTermDTO> getUserLevelTerms() {
		return userLevelTerms;
	}

	public void setUserLevelTerms(List<UserLevelTermDTO> userLevelTerms) {
		this.userLevelTerms = userLevelTerms;
	}

	public Integer getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(Integer recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	@Override
	public String toString() {
		return "UserLevelDTO{" +
				"userLevelId=" + userLevelId +
				", level=" + level +
				", levelName='" + levelName + '\'' +
				", levelType=" + levelType +
				", needGrowth=" + needGrowth +
				", updateStatus=" + updateStatus +
				", recruitStatus=" + recruitStatus +
				", userRightsIds=" + userRightsIds +
				", userLevelTerms=" + userLevelTerms +
				'}';
	}
}
