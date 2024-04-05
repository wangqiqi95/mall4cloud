package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public class DistributionUserBind extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户关系表
     */
    private Long bindId;

    /**
     * 分销员id
     */
    private Long distributionUserId;

    /**
     * 用户id
     */
    private Long userId;

    /**
	 * BindStateEnum
     * 当前绑定关系(-1失效 0 预绑定 1生效)
     */
    private Integer state;

    /**
	 * BindInvalidReasonEnum
     * 失效原因( 1管理员更改 2封禁)
     */
    private Integer invalidReason;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 失效时间
     */
    private Date invalidTime;

	public Long getBindId() {
		return bindId;
	}

	public void setBindId(Long bindId) {
		this.bindId = bindId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(Integer invalidReason) {
		this.invalidReason = invalidReason;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	@Override
	public String toString() {
		return "DistributionUserBind{" +
				"bindId=" + bindId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",distributionUserId=" + distributionUserId +
				",userId=" + userId +
				",state=" + state +
				",invalidReason=" + invalidReason +
				",bindTime=" + bindTime +
				",invalidTime=" + invalidTime +
				'}';
	}
}
