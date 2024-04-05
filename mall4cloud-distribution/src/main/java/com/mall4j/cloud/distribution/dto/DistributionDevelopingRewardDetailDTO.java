package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-发展奖励明细DTO
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public class DistributionDevelopingRewardDetailDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("发展奖励活动ID")
    private Long developingRewardId;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("导购编号")
    private String staffCode;

    @ApiModelProperty("导购名称")
    private String staffName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("发展威客数量")
    private Integer memberNum;

    @ApiModelProperty("累计奖励")
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
		return "DistributionDevelopingRewardDetailDTO{" +
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
				'}';
	}
}
