package com.mall4j.cloud.biz.service;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveOrderService {

    /**
     * 记录订单变更信息
     *
     * @param orderId
     * @return
     */
    void recordOrderChange(String orderId);

    /**
     * 记录订单售后变更信息
     *
     * @param orderId
     * @return
     */
    void recordAftersaleOrderChange(String orderId);
}
