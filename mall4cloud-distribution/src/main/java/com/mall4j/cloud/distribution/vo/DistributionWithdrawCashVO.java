package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员提现记录VO
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionWithdrawCashVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提现记录id")
    private Long withdrawCashId;

    @ApiModelProperty("钱包id")
    private Long walletId;

    @ApiModelProperty("金额")
    private Long amount;

    @ApiModelProperty("手续费")
    private Long fee;

    @ApiModelProperty("类型(0 手动提现 1自动提现)")
    private Integer type;

    @ApiModelProperty("资金流向(0微信红包、1企业付款到微信零钱)")
    private Integer moneyFlow;

    @ApiModelProperty("商户订单号")
    private String merchantOrderId;

    @ApiModelProperty("乐观锁")
    private Integer version;

    @ApiModelProperty("提现状态(0:申请中 1:提现成功 -1:提现失败)")
    private Integer state;

	@ApiModelProperty("分销员信息")
	private DistributionUserVO distributionUserVO;

	public Long getWithdrawCashId() {
		return withdrawCashId;
	}

	public void setWithdrawCashId(Long withdrawCashId) {
		this.withdrawCashId = withdrawCashId;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMoneyFlow() {
		return moneyFlow;
	}

	public void setMoneyFlow(Integer moneyFlow) {
		this.moneyFlow = moneyFlow;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
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

	public DistributionUserVO getDistributionUserVO() {
		return distributionUserVO;
	}

	public void setDistributionUserVO(DistributionUserVO distributionUserVO) {
		this.distributionUserVO = distributionUserVO;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawCashVO{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", withdrawCashId=" + withdrawCashId +
				", walletId=" + walletId +
				", amount=" + amount +
				", fee=" + fee +
				", type=" + type +
				", moneyFlow=" + moneyFlow +
				", merchantOrderId='" + merchantOrderId + '\'' +
				", version=" + version +
				", state=" + state +
				", distributionUserVO=" + distributionUserVO +
				'}';
	}
}
