package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionWithdrawCash extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 提现记录id
     */
    private Long withdrawCashId;

    /**
     * 钱包id
     */
    private Long walletId;

    /**
     * 金额
     */
    private Long amount;

    /**
     * 手续费
     */
    private Long fee;

    /**
     * 类型(0 手动提现 1自动提现)
     */
    private Integer type;

    /**
     * 资金流向(0微信红包、1企业付款到微信零钱)
     */
    private Integer moneyFlow;

    /**
     * 商户订单号
     */
    private String merchantOrderId;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
	 * DistributionWithdrawCashStateEnum
     * 提现状态(0:申请中 1:提现成功 -1:提现失败)
     */
    private Integer state;

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

	@Override
	public String toString() {
		return "DistributionWithdrawCash{" +
				"withdrawCashId=" + withdrawCashId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",walletId=" + walletId +
				",amount=" + amount +
				",fee=" + fee +
				",type=" + type +
				",moneyFlow=" + moneyFlow +
				",merchantOrderId=" + merchantOrderId +
				",version=" + version +
				",state=" + state +
				'}';
	}
}
