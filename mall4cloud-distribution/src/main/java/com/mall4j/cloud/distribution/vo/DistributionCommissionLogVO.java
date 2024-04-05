package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 佣金流水信息VO
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public class DistributionCommissionLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("变更值")
    private Long changeValue;

    @ApiModelProperty("1增加 -1减少")
    private Integer operation;

    @ApiModelProperty("变更类型")
    private Integer type;

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

	public Long getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(Long changeValue) {
		this.changeValue = changeValue;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DistributionCommissionLogVO{" +
				"id=" + id +
				",identityType=" + identityType +
				",userId=" + userId +
				",changeValue=" + changeValue +
				",operation=" + operation +
				",type=" + type +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
