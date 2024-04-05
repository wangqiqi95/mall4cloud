package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 佣金管理-佣金提现配置VO
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
public class DistributionWithdrawConfigVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("提现最小范围")
    private Integer withdrawMin;

    @ApiModelProperty("提现最大范围")
    private Integer withdrawMax;

    @ApiModelProperty("提现频次类型 0 无限制 1 指定范围 2指定日期")
    private Integer frequencyType;

    @ApiModelProperty("提现范围类型 1每日 2每周 3每月")
    private Integer scopeType;

    @ApiModelProperty("指定范围提现次数")
    private Integer scopeCount;

    @ApiModelProperty("指定提现日期")
    private Integer specifyDate;

    @ApiModelProperty("指定提现日期次数")
    private Integer specifyCount;

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

	public Integer getWithdrawMin() {
		return withdrawMin;
	}

	public void setWithdrawMin(Integer withdrawMin) {
		this.withdrawMin = withdrawMin;
	}

	public Integer getWithdrawMax() {
		return withdrawMax;
	}

	public void setWithdrawMax(Integer withdrawMax) {
		this.withdrawMax = withdrawMax;
	}

	public Integer getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(Integer frequencyType) {
		this.frequencyType = frequencyType;
	}

	public Integer getScopeType() {
		return scopeType;
	}

	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	public Integer getScopeCount() {
		return scopeCount;
	}

	public void setScopeCount(Integer scopeCount) {
		this.scopeCount = scopeCount;
	}

	public Integer getSpecifyDate() {
		return specifyDate;
	}

	public void setSpecifyDate(Integer specifyDate) {
		this.specifyDate = specifyDate;
	}

	public Integer getSpecifyCount() {
		return specifyCount;
	}

	public void setSpecifyCount(Integer specifyCount) {
		this.specifyCount = specifyCount;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawConfigVO{" +
				"id=" + id +
				",identityType=" + identityType +
				",withdrawMin=" + withdrawMin +
				",withdrawMax=" + withdrawMax +
				",frequencyType=" + frequencyType +
				",scopeType=" + scopeType +
				",scopeCount=" + scopeCount +
				",specifyDate=" + specifyDate +
				",specifyCount=" + specifyCount +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
