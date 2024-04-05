package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
public class DistributionWithdrawProcessLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 处理批次ID
     */
    private Long processId;

    /**
     * 提现单号
     */
    private String withdrawOrderNo;

    /**
     * 状态 0成功 1失败
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String failReason;

	/**
	 * 处理类型 1通过 2拒绝
	 */
	private Integer processType;

	/**
	 * 拒绝原因
	 */
	private String rejectReason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getWithdrawOrderNo() {
		return withdrawOrderNo;
	}

	public void setWithdrawOrderNo(String withdrawOrderNo) {
		this.withdrawOrderNo = withdrawOrderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawProcessLog{" +
				"id=" + id +
				", processId=" + processId +
				", withdrawOrderNo='" + withdrawOrderNo + '\'' +
				", status=" + status +
				", failReason='" + failReason + '\'' +
				", processType=" + processType +
				", rejectReason='" + rejectReason + '\'' +
				'}';
	}
}
