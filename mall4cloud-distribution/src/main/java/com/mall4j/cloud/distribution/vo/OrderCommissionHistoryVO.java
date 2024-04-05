package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 历史提现订单VO
 *
 * @author ZengFanChang
 * @date 2022-04-26
 */
public class OrderCommissionHistoryVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1-导购 2-微客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("历史可提现佣金")
    private Long commission;

    @ApiModelProperty("提现状态 0:待提现 1:提现中 2:提现成功 3:提现失败")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCommission() {
		return commission;
	}

	public void setCommission(Long commission) {
		this.commission = commission;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OrderCommissionHistoryVO{" +
				"id=" + id +
				",identityType=" + identityType +
				",userId=" + userId +
				",commission=" + commission +
				",status=" + status +
				",remark=" + remark +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
