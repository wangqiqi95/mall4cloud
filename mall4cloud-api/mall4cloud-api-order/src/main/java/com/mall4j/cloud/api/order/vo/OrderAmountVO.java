package com.mall4j.cloud.api.order.vo;

/**
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
public class OrderAmountVO {

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 支付积分
     */
    private Long payScore;

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getPayScore() {
        return payScore;
    }

    public void setPayScore(Long payScore) {
        this.payScore = payScore;
    }

    @Override
    public String toString() {
        return "OrderAmountVO{" +
                "payAmount=" + payAmount +
                ", payScore=" + payScore +
                '}';
    }
}
