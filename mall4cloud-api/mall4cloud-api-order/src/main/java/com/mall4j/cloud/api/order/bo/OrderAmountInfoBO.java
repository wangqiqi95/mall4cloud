package com.mall4j.cloud.api.order.bo;

/**
 * @author FrozenWatermelon
 * @date 2021/1/13
 */
public class OrderAmountInfoBO extends OrderSimpleAmountInfoBO{

    /**
     * 退款成功金额
     */
    private Long refundSuccessAmount;

    /**
     * 平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
     */
    private Long platformRefundAmount;

    public Long getRefundSuccessAmount() {
        return refundSuccessAmount;
    }

    public void setRefundSuccessAmount(Long refundSuccessAmount) {
        this.refundSuccessAmount = refundSuccessAmount;
    }

    public Long getPlatformRefundAmount() {
        return platformRefundAmount;
    }

    public void setPlatformRefundAmount(Long platformRefundAmount) {
        this.platformRefundAmount = platformRefundAmount;
    }

    @Override
    public String toString() {
        return "OrderAmountInfoBO{" +
                "refundSuccessAmount=" + refundSuccessAmount +
                '}';
    }
}
