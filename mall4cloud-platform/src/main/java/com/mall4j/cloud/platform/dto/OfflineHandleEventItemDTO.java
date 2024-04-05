package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 下线处理事件记录项DTO
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public class OfflineHandleEventItemDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty()
    private Long eventItemId;

    @ApiModelProperty("事件id")
    private Long eventId;

    @ApiModelProperty("重新申请上线理由")
    private String reapplyReason;

    @ApiModelProperty("拒绝原因")
    private String refuseReason;

    @ApiModelProperty("重新申请上线时间")
    private Date reapplyTime;

    @ApiModelProperty("审核时间")
    private Date auditTime;

	public Long getEventItemId() {
		return eventItemId;
	}

	public void setEventItemId(Long eventItemId) {
		this.eventItemId = eventItemId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getReapplyReason() {
		return reapplyReason;
	}

	public void setReapplyReason(String reapplyReason) {
		this.reapplyReason = reapplyReason;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Date getReapplyTime() {
		return reapplyTime;
	}

	public void setReapplyTime(Date reapplyTime) {
		this.reapplyTime = reapplyTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	@Override
	public String toString() {
		return "OfflineHandleEventItemDTO{" +
				"eventItemId=" + eventItemId +
				",eventId=" + eventId +
				",reapplyReason=" + reapplyReason +
				",refuseReason=" + refuseReason +
				",reapplyTime=" + reapplyTime +
				",auditTime=" + auditTime +
				'}';
	}
}
