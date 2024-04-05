package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 佣金处理批次记录DTO
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
public class DistributionWithdrawProcessLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("处理批次ID")
    private Long processId;

    @ApiModelProperty("提现单号")
    private String withdrawOrderNo;

    @ApiModelProperty("状态 0成功 1失败")
    private Integer status;

    @ApiModelProperty("失败原因")
    private String failReason;

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

	@Override
	public String toString() {
		return "DistributionWithdrawProcessLogDTO{" +
				"id=" + id +
				",processId=" + processId +
				",withdrawOrderNo=" + withdrawOrderNo +
				",status=" + status +
				",failReason=" + failReason +
				'}';
	}
}
