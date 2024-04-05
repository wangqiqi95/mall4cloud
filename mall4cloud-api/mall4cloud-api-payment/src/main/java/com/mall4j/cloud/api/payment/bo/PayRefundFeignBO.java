package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

/**
 * @luzhengxiang
 * @create 2022-05-16 2:06 AM
 **/
@Data
public class PayRefundFeignBO {
    /**
     * 退款单号
     */
    private Long refundId;

    /**
     * 关联的支付订单id
     */
    private Long orderId;

    /**
     * 关联的支付单id
     */
    private Long payId;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 是否直接退款
     */
    private Integer onlyRefund;

    /**
     * 是否为未成团而退款的团购订单
     */
    private Integer unSuccessGroupOrder;
}
