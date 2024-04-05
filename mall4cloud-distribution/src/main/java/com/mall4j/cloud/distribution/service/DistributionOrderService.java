package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.distribution.dto.DistributionOrderQueryDTO;

import java.util.List;

/**
 * @author cl
 * @date 2021-08-18 08:40:06
 */
public interface DistributionOrderService {

    /**
     * 订单支付成功开始进行分销
     * @param payNotifyBO 支付成功通知
     */
    void payNotifyDistributionOrder(PayNotifyBO payNotifyBO);


    /**
     * 订单支付成功开始进行分销
     * @param payNotifyBO 支付成功通知
     */
    void paySuccessNotifyDistributionOrder(PayNotifyBO payNotifyBO);


}
