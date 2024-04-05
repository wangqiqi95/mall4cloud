package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 佣金提现订单信息VO
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public class DistributionWithdrawOrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("提现记录ID")
    private Long recordId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单号")
    private Long orderId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawOrderVO{" +
				"id=" + id +
				",identityType=" + identityType +
				",recordId=" + recordId +
				",userId=" + userId +
				",orderNo=" + orderId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
