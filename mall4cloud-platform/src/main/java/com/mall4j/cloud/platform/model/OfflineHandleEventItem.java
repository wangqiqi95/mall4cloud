package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 下线处理事件记录项
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public class OfflineHandleEventItem extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long eventItemId;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 重新申请上线理由
     */
    private String reapplyReason;

    /**
     * 拒绝原因
     */
    private String refuseReason;

    /**
     * 重新申请上线时间
     */
    private Date reapplyTime;

    /**
     * 审核时间
     */
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
		return "OfflineHandleEventItem{" +
				"eventItemId=" + eventItemId +
				",createTime=" + createTime +
				",eventId=" + eventId +
				",reapplyReason=" + reapplyReason +
				",refuseReason=" + refuseReason +
				",reapplyTime=" + reapplyTime +
				",auditTime=" + auditTime +
				'}';
	}
}
