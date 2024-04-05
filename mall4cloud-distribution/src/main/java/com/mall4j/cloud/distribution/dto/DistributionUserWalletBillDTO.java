package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员钱包流水记录DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionUserWalletBillDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("钱包流水记录")
    private Long id;

    @ApiModelProperty("钱包id")
    private Long walletId;

    @ApiModelProperty("待结算金额变更数额")
    private Long unsettledAmount;

    @ApiModelProperty("可提现金额变更数额")
    private Long settledAmount;

    @ApiModelProperty("失效金额变更数额")
    private Long invalidAmount;

    @ApiModelProperty("积累收益变更数额")
    private Long accumulateAmount;

    @ApiModelProperty("英文备注")
    private String remarksEn;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("变更后待结算金额")
    private Long unsettledAmountAfter;

    @ApiModelProperty("变更后可提现金额")
    private Long settledAmountAfter;

    @ApiModelProperty("变更后失效金额")
    private Long invalidAmountAfter;

    @ApiModelProperty("变更后积累收益")
    private Long accumulateAmountAfter;

    @ApiModelProperty("类型(0 系统修改 1人工修改)")
    private Integer type;

    @ApiModelProperty("操作人id")
    private Long modifier;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Long getUnsettledAmount() {
		return unsettledAmount;
	}

	public void setUnsettledAmount(Long unsettledAmount) {
		this.unsettledAmount = unsettledAmount;
	}

	public Long getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Long settledAmount) {
		this.settledAmount = settledAmount;
	}

	public Long getInvalidAmount() {
		return invalidAmount;
	}

	public void setInvalidAmount(Long invalidAmount) {
		this.invalidAmount = invalidAmount;
	}

	public Long getAccumulateAmount() {
		return accumulateAmount;
	}

	public void setAccumulateAmount(Long accumulateAmount) {
		this.accumulateAmount = accumulateAmount;
	}

	public String getRemarksEn() {
		return remarksEn;
	}

	public void setRemarksEn(String remarksEn) {
		this.remarksEn = remarksEn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getUnsettledAmountAfter() {
		return unsettledAmountAfter;
	}

	public void setUnsettledAmountAfter(Long unsettledAmountAfter) {
		this.unsettledAmountAfter = unsettledAmountAfter;
	}

	public Long getSettledAmountAfter() {
		return settledAmountAfter;
	}

	public void setSettledAmountAfter(Long settledAmountAfter) {
		this.settledAmountAfter = settledAmountAfter;
	}

	public Long getInvalidAmountAfter() {
		return invalidAmountAfter;
	}

	public void setInvalidAmountAfter(Long invalidAmountAfter) {
		this.invalidAmountAfter = invalidAmountAfter;
	}

	public Long getAccumulateAmountAfter() {
		return accumulateAmountAfter;
	}

	public void setAccumulateAmountAfter(Long accumulateAmountAfter) {
		this.accumulateAmountAfter = accumulateAmountAfter;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return "DistributionUserWalletBillDTO{" +
				"id=" + id +
				", walletId=" + walletId +
				", unsettledAmount=" + unsettledAmount +
				", settledAmount=" + settledAmount +
				", invalidAmount=" + invalidAmount +
				", accumulateAmount=" + accumulateAmount +
				", remarksEn='" + remarksEn + '\'' +
				", remarks='" + remarks + '\'' +
				", unsettledAmountAfter=" + unsettledAmountAfter +
				", settledAmountAfter=" + settledAmountAfter +
				", invalidAmountAfter=" + invalidAmountAfter +
				", accumulateAmountAfter=" + accumulateAmountAfter +
				", type=" + type +
				", modifier=" + modifier +
				'}';
	}
}
