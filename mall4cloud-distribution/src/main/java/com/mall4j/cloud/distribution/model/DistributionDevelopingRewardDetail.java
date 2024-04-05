package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-发展奖励明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public class DistributionDevelopingRewardDetail extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发展奖励活动ID
     */
    private Long developingRewardId;

    /**
     * 导购ID
     */
    private Long staffId;

    /**
     * 导购编号
     */
    private String staffCode;

    /**
     * 导购名称
     */
    private String staffName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 发展威客数量
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

	public Long getDevelopingRewardId() {
		return developingRewardId;
	}

	public void setDevelopingRewardId(Long developingRewardId) {
		this.developingRewardId = developingRewardId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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
		return "DistributionDevelopingRewardDetail{" +
				"id=" + id +
				",developingRewardId=" + developingRewardId +
				",staffId=" + staffId +
				",staffCode=" + staffCode +
				",staffName=" + staffName +
				",mobile=" + mobile +
				",storeId=" + storeId +
				",storeName=" + storeName +
				",memberNum=" + memberNum +
				",totalReward=" + totalReward +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
