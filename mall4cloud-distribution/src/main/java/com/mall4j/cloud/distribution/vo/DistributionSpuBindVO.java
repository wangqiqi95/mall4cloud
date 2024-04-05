package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 用户商品绑定信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionSpuBindVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户商品绑定表")
    private Long id;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("绑定时间")
    private Date bindTime;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("状态(0失效 1生效) 分销员被封禁 该状态失效")
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
		return "DistributionSpuBindVO{" +
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
