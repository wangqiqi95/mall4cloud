package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.multishop.bo.OrderChangeShopWalletAmountBO;

/**
 * 退款，分销部分
 * @author cl
 * @date 2021-08-20 15:46:55
 */
public interface DistributionRefundService {

    /**
     * 退款，回退分销佣金
     * @param message 退款信息
     */
    void refundDistributionAmount(OrderChangeShopWalletAmountBO message);

    /**
     * 退款，回退分销佣金
     * @param message 退款信息
     */
    void refundSuccessDistributionAmount(OrderChangeShopWalletAmountBO message);

}
