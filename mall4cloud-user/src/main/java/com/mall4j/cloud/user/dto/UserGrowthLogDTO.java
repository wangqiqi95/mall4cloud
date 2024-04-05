package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户成长值记录DTO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserGrowthLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成长值获取记录表")
    private Long logId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("来源 1 订单")
    private Integer source;

    @ApiModelProperty("关联业务id")
    private Long bizId;

    @ApiModelProperty("变更成长值")
    private Integer changeGrowth;

    @ApiModelProperty("备注")
    private String remarks;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Integer getChangeGrowth() {
		return changeGrowth;
	}

	public void setChangeGrowth(Integer changeGrowth) {
		this.changeGrowth = changeGrowth;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "UserGrowthLogDTO{" +
				"logId=" + logId +
				",userId=" + userId +
				",source=" + source +
				",bizId=" + bizId +
				",changeGrowth=" + changeGrowth +
				",remarks=" + remarks +
				'}';
	}
}
