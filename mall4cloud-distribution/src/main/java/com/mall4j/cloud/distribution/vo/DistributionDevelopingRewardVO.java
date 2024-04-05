package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-发展奖励VO
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public class DistributionDevelopingRewardVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("活动编号")
    private Integer numbering;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

    @ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

    @ApiModelProperty("奖励金额")
    private Long rewardAmount;

    @ApiModelProperty("最多奖励人数")
    private Integer rewardMax;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("权重")
    private Integer index;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("导购数量")
    private Integer staffNum;

    @ApiModelProperty("威客数量")
    private Integer memberNum;

    @ApiModelProperty("累计奖励")
    private Long totalReward;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumbering() {
		return numbering;
	}

	public void setNumbering(Integer numbering) {
		this.numbering = numbering;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public Integer getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}

	public Long getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(Long rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public Integer getRewardMax() {
		return rewardMax;
	}

	public void setRewardMax(Integer rewardMax) {
		this.rewardMax = rewardMax;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(Integer staffNum) {
		this.staffNum = staffNum;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Long getTotalReward() {
		return totalReward;
	}

	public void setTotalReward(Long totalReward) {
		this.totalReward = totalReward;
	}

	@Override
	public String toString() {
		return "DistributionDevelopingRewardVO{" +
				"id=" + id +
				",numbering=" + numbering +
				",storeType=" + storeType +
				",distributionType=" + distributionType +
				",rewardAmount=" + rewardAmount +
				",rewardMax=" + rewardMax +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",index=" + index +
				",status=" + status +
				",staffNum=" + staffNum +
				",memberNum=" + memberNum +
				",totalReward=" + totalReward +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
