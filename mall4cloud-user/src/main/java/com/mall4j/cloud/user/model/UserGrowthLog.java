package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserGrowthLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 成长值获取记录表
     */
    private Long logId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 来源 0系统修改 1 订单
     */
    private Integer source;

    /**
     * 关联业务id
     */
    private Long bizId;

    /**
     * 变更成长值
     */
    private Integer changeGrowth;

    /**
     * 备注
     */
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
		return "UserGrowthLog{" +
				"logId=" + logId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",source=" + source +
				",bizId=" + bizId +
				",changeGrowth=" + changeGrowth +
				",remarks=" + remarks +
				'}';
	}
}
