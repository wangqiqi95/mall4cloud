package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销员申请信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public class DistributionAuditing extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分销员申请表
     */
    private Long auditingId;

    /**
     * 邀请人id
     */
    private Long parentDistributionUserId;

    /**
     * 申请人id
     */
    private Long distributionUserId;

    /**
     * 申请时间
     */
    private Date auditingTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 不通过原因(0 资料不足 1条件不足 2不招人 -1其他)
     */
    private Integer reason;

    /**
     * 审核状态：0 未审核 1已通过 -1未通过
     */
    private Integer state;

    /**
     * 操作人(0代表自动审核)
     */
    private Long modifier;

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

	@Override
	public String toString() {
		return "DistributionAuditing{" +
				"auditingId=" + auditingId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",parentDistributionUserId=" + parentDistributionUserId +
				",distributionUserId=" + distributionUserId +
				",auditingTime=" + auditingTime +
				",remarks=" + remarks +
				",reason=" + reason +
				",state=" + state +
				",modifier=" + modifier +
				'}';
	}
}
