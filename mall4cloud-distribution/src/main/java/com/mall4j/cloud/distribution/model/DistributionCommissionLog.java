package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public class DistributionCommissionLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 身份类型 1导购 2威客
     */
    private Integer identityType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 变更值
     */
    private Long changeValue;

    /**
     * 1增加 -1减少
     */
    private Integer operation;

    /**
     * 变更类型
     */
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
		return "DistributionCommissionLog{" +
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
