package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-发展奖励
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public class DistributionDevelopingReward extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动编号
     */
    private Integer numbering;

    /**
     * 选择门店类型 0全部门店 1部分门店
     */
    private Integer storeType;

    /**
     * 分销类型 0全部 1导购 2威客
     */
    private Integer distributionType;

    /**
     * 奖励金额
     */
    private Long rewardAmount;

    /**
     * 最多奖励人数
     */
    private Integer rewardMax;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 权重
     */
    private Integer index;

    /**
     * 状态 1启用 0禁用
     */
    private Integer status;

    /**
     * 导购数量
     */
    private Integer staffNum;

    /**
     * 威客数量
     */
    private Integer memberNum;

    /**
     * 累计奖励
     */
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
		return "DistributionDevelopingReward{" +
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
