package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionUserWallet extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分销员钱包id
     */
    private Long walletId;

    /**
     * 分销员id
     */
    private Long distributionUserId;

    /**
     * 待结算金额
     */
    private Long unsettledAmount;

    /**
     * 可提现金额
     */
    private Long settledAmount;

    /**
     * 已失效金额
     */
    private Long invalidAmount;

    /**
     * 积累收益
     */
    private Long accumulateAmount;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 钱包状态(-1 已冻结 0未生效(分销审核未通过) 1正常)
     */
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
		return "DistributionUserWallet{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", walletId=" + walletId +
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
