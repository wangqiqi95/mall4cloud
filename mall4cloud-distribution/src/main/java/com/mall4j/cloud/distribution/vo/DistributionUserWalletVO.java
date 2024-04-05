package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员钱包信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public class DistributionUserWalletVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销员钱包id")
    private Long walletId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("待结算金额")
    private Long unsettledAmount;

    @ApiModelProperty("可提现金额")
    private Long settledAmount;

    @ApiModelProperty("已失效金额")
    private Long invalidAmount;

    @ApiModelProperty("积累收益")
    private Long accumulateAmount;

    @ApiModelProperty("乐观锁")
    private Integer version;

    @ApiModelProperty("钱包状态(-1 已冻结 0未生效(分销审核未通过) 1正常)")
    private Integer state;

    @ApiModelProperty("分销员信息")
    private DistributionUserVO distributionUserVO;

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

	public DistributionUserVO getDistributionUserVO() {
		return distributionUserVO;
	}

	public void setDistributionUserVO(DistributionUserVO distributionUserVO) {
		this.distributionUserVO = distributionUserVO;
	}

	@Override
	public String toString() {
		return "DistributionUserWalletVO{" +
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
				", distributionUserVO=" + distributionUserVO +
				'}';
	}
}
