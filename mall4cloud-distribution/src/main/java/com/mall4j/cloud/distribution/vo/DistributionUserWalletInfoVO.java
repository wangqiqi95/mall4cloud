package com.mall4j.cloud.distribution.vo;

/**
 * @author Citrus
 * @date 2021/8/19 14:59
 */
public class DistributionUserWalletInfoVO {
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

    @Override
    public String toString() {
        return "DistributionUserWalletInfoVO{" +
                "unsettledAmount=" + unsettledAmount +
                ", settledAmount=" + settledAmount +
                ", invalidAmount=" + invalidAmount +
                ", accumulateAmount=" + accumulateAmount +
                '}';
    }
}
