package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 会员等级表VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long userLevelId;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("vip等级")
    private Integer vipLevel;

    @ApiModelProperty("等级类型 0:普通会员 1:付费会员")
    private Integer levelType;

    @ApiModelProperty("所需成长值")
    private Integer needGrowth;

	@ApiModelProperty(value = "下一等级名称")
	private String nextLevelName;

	@ApiModelProperty(value = "下一等级所需成长值")
	private Integer nextGrowth;

    @ApiModelProperty("1:已更新 0:等待更新(等级修改后，用户等级的更新)")
    private Integer updateStatus;

	@ApiModelProperty("付费会员，是否可以招募会员；1可以招募，0停止招募")
	private Integer recruitStatus;

	@ApiModelProperty("用户权益Ids")
	private List<Long> userRightsIds;

	@ApiModelProperty("用户权益列表")
	private List<UserRightsVO> userRightsList;

	@ApiModelProperty("优惠券id列表")
	private List<Long> couponIds;

	@ApiModelProperty("优惠券张数")
	private Integer couponsNum;

	@ApiModelProperty("等级期数id列表")
	private List<Long> levelTermIds;

	@ApiModelProperty("等级期数列表")
	private List<UserLevelTermVO> userLevelTerms;

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

	public List<UserRightsVO> getUserRightsList() {
		return userRightsList;
	}

	public void setUserRightsList(List<UserRightsVO> userRightsList) {
		this.userRightsList = userRightsList;
	}

	public Integer getCouponsNum() {
		return couponsNum;
	}

	public void setCouponsNum(Integer couponsNum) {
		this.couponsNum = couponsNum;
	}

	public String getNextLevelName() {
		return nextLevelName;
	}

	public void setNextLevelName(String nextLevelName) {
		this.nextLevelName = nextLevelName;
	}

	public Integer getNextGrowth() {
		return nextGrowth;
	}

	public void setNextGrowth(Integer nextGrowth) {
		this.nextGrowth = nextGrowth;
	}

	public List<Long> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Long> couponIds) {
		this.couponIds = couponIds;
	}

	public List<UserLevelTermVO> getUserLevelTerms() {
		return userLevelTerms;
	}

	public void setUserLevelTerms(List<UserLevelTermVO> userLevelTerms) {
		this.userLevelTerms = userLevelTerms;
	}

	public List<Long> getUserLevelTermIds() {
		return levelTermIds;
	}

	public void setUserLevelTermIds(List<Long> levelTermIds) {
		this.levelTermIds = levelTermIds;
	}

	public Integer getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(Integer recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	@Override
	public String toString() {
		return "UserLevelVO{" +
				"userLevelId=" + userLevelId +
				", level=" + level +
				", levelName='" + levelName + '\'' +
				", vipLevel=" + vipLevel +
				", levelType=" + levelType +
				", needGrowth=" + needGrowth +
				", nextLevelName='" + nextLevelName + '\'' +
				", nextGrowth=" + nextGrowth +
				", updateStatus=" + updateStatus +
				", recruitStatus=" + recruitStatus +
				", userRightsIds=" + userRightsIds +
				", userRightsList=" + userRightsList +
				", couponIds=" + couponIds +
				", couponsNum=" + couponsNum +
				", levelTermIds=" + levelTermIds +
				", userLevelTerms=" + userLevelTerms +
				'}';
	}
}
