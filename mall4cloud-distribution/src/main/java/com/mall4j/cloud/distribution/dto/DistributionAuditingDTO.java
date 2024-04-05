package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员申请信息DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public class DistributionAuditingDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销员申请表")
    private Long auditingId;

    @ApiModelProperty("邀请人id")
    private Long parentDistributionUserId;

    @ApiModelProperty("申请人id")
    private Long distributionUserId;

    @ApiModelProperty("申请时间")
    private Date auditingTime;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("不通过原因(0 资料不足 1条件不足 2不招人 -1其他)")
    private Integer reason;

    @ApiModelProperty("审核状态：0 未审核 1已通过 -1未通过")
    private Integer state;

    @ApiModelProperty("操作人(0代表自动审核)")
    private Long modifier;

	@ApiModelProperty("排序字段：分销员审核：0无 1申请时间")
	private Integer sortParam;

	@ApiModelProperty("排序类型 0无 1 正序 2倒序")
	private Integer sortType;

	@ApiModelProperty("分销员手机号")
	private String userMobile;

	public Long getAuditingId() {
		return auditingId;
	}

	public void setAuditingId(Long auditingId) {
		this.auditingId = auditingId;
	}

	public Long getParentDistributionUserId() {
		return parentDistributionUserId;
	}

	public void setParentDistributionUserId(Long parentDistributionUserId) {
		this.parentDistributionUserId = parentDistributionUserId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Date getAuditingTime() {
		return auditingTime;
	}

	public void setAuditingTime(Date auditingTime) {
		this.auditingTime = auditingTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getReason() {
		return reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
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

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	@Override
	public String toString() {
		return "DistributionAuditingDTO{" +
				"auditingId=" + auditingId +
				", parentDistributionUserId=" + parentDistributionUserId +
				", distributionUserId=" + distributionUserId +
				", auditingTime=" + auditingTime +
				", remarks='" + remarks + '\'' +
				", reason=" + reason +
				", state=" + state +
				", modifier=" + modifier +
				", sortParam=" + sortParam +
				", sortType=" + sortType +
				", userMobile='" + userMobile + '\'' +
				'}';
	}
}
