package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销封禁记录
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
public class DistributionUserBan extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long banId;

    /**
     * 分销员id
     */
    private Long distributionUserId;

    /**
     * 原因(0 失去联系 1恶意刷单 2其他)
     */
    private Integer banReason;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态(1 正常 2暂时封禁 -1永久封禁)
     */
    private Integer state;

    /**
     * 修改人
     */
    private Long modifier;

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

	@Override
	public String toString() {
		return "DistributionUserBan{" +
				"banId=" + banId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",distributionUserId=" + distributionUserId +
				",banReason=" + banReason +
				",remarks=" + remarks +
				",state=" + state +
				",modifier=" + modifier +
				'}';
	}
}
