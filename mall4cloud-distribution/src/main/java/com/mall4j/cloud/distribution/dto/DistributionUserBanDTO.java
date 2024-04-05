package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销封禁记录DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
public class DistributionUserBanDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long banId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("原因(0 失去联系 1恶意刷单 2其他)")
    private Integer banReason;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("状态(1 正常 2暂时封禁 -1永久封禁)")
    private Integer state;

    @ApiModelProperty("修改人")
    private Long modifier;

    @ApiModelProperty("分销员手机号")
    private String userMobile;

	@ApiModelProperty("排序字段：分销管理-分销员-封禁记录： 0无 1操作时间")
	private Integer sortParam;

	@ApiModelProperty("排序类型 0无 1 正序 2倒序")
	private Integer sortType;

	public Long getBanId() {
		return banId;
	}

	public void setBanId(Long banId) {
		this.banId = banId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Integer getBanReason() {
		return banReason;
	}

	public void setBanReason(Integer banReason) {
		this.banReason = banReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Integer getSortParam() {
		return sortParam;
	}

	public void setSortParam(Integer sortParam) {
		this.sortParam = sortParam;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		return "DistributionUserBanDTO{" +
				"banId=" + banId +
				", distributionUserId=" + distributionUserId +
				", banReason=" + banReason +
				", remarks='" + remarks + '\'' +
				", state=" + state +
				", modifier=" + modifier +
				", userMobile='" + userMobile + '\'' +
				", sortParam=" + sortParam +
				", sortType=" + sortType +
				'}';
	}
}
