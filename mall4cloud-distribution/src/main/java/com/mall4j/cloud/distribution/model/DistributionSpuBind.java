package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户商品绑定信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionSpuBind extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户商品绑定表
     */
    private Long id;

    /**
     * 分销员id
     */
    private Long distributionUserId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 状态(0失效 1生效) 分销员被封禁 该状态失效
     */
    private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "DistributionSpuBind{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", id=" + id +
				", distributionUserId=" + distributionUserId +
				", userId=" + userId +
				", bindTime=" + bindTime +
				", spuId=" + spuId +
				", state=" + state +
				'}';
	}
}
