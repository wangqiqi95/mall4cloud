package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.common.util.PriceUtil;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销员钱包信息DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionUserWalletDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销员钱包id")
    private Long walletId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

	@ApiModelProperty("待结算金额")
	@Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不能超过" + PriceUtil.MAX_AMOUNT)
	@Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private BigDecimal unsettledAmount;

    @ApiModelProperty("可提现金额")
	@Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不能超过" + PriceUtil.MAX_AMOUNT)
	@Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private BigDecimal settledAmount;

    @ApiModelProperty("已失效金额")
	@Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不能超过" + PriceUtil.MAX_AMOUNT)
	@Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private BigDecimal invalidAmount;

    @ApiModelProperty("积累收益")
	@Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不能超过" + PriceUtil.MAX_AMOUNT)
	@Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private BigDecimal accumulateAmount;

    @ApiModelProperty("乐观锁")
    private Integer version;

    @ApiModelProperty("钱包状态(-1 已冻结 0未生效(分销审核未通过) 1正常)")
    private Integer state;

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public BigDecimal getUnsettledAmount() {
		return unsettledAmount;
	}

	public void setUnsettledAmount(BigDecimal unsettledAmount) {
		this.unsettledAmount = unsettledAmount;
	}

	public BigDecimal getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(BigDecimal settledAmount) {
		this.settledAmount = settledAmount;
	}

	public BigDecimal getInvalidAmount() {
		return invalidAmount;
	}

	public void setInvalidAmount(BigDecimal invalidAmount) {
		this.invalidAmount = invalidAmount;
	}

	public BigDecimal getAccumulateAmount() {
		return accumulateAmount;
	}

	public void setAccumulateAmount(BigDecimal accumulateAmount) {
		this.accumulateAmount = accumulateAmount;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "DistributionUserWalletDTO{" +
				"walletId=" + walletId +
				", distributionUserId=" + distributionUserId +
				", unsettledAmount=" + unsettledAmount +
				", settledAmount=" + settledAmount +
				", invalidAmount=" + invalidAmount +
				", accumulateAmount=" + accumulateAmount +
				", version=" + version +
				", state=" + state +
				'}';
	}
}
