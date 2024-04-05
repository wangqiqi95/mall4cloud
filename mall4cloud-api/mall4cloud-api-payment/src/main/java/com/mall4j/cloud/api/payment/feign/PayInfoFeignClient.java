package com.mall4j.cloud.api.payment.feign;

import com.mall4j.cloud.api.payment.bo.LiveStoreOrderCancelInfoVO;
import com.mall4j.cloud.api.payment.bo.LiveStoreOrderRefundInfoVO;
import com.mall4j.cloud.api.payment.bo.LiveStorePayInfoVO;
import com.mall4j.cloud.api.payment.bo.SQBOrderPaySuccessBO;
import com.mall4j.cloud.api.payment.vo.GetPayInfoByOrderIdsAndPayTypeVO;
import com.mall4j.cloud.api.payment.vo.PayInfoFeignVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 支付feign处理类
 *
 * @luzhengxiang
 * @create 2022-05-18 21:29 PM
 **/
@FeignClient(value = "mall4cloud-payment",contextId = "payinfo")
public interface PayInfoFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pay/getPayInfoByOrderIds")
    ServerResponseEntity<List<PayInfoFeignVO>> getPayInfoByOrderids(@RequestBody List<Long> orderIds);
    
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pay/getNotPayPayInfoByOrderids")
    ServerResponseEntity<List<PayInfoFeignVO>> getNotPayPayInfoByOrderids(@RequestBody List<Long> orderIds);


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pay/cancelWechatPayOrder")
    ServerResponseEntity<Boolean> cancelWechatPayOrder(@RequestBody String orderNumber);


    /**
     * 支付成功
     * @param liveStorePayInfoVO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/livestore/pay/success/")
    ServerResponseEntity<Boolean> livestorePay(@RequestBody LiveStorePayInfoVO liveStorePayInfoVO);

    /**
     * 订单取消
     * @param liveStorePayInfoVO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/livestore/order/cancel/success/")
    ServerResponseEntity<Boolean> livestoreOrderCancel(@RequestBody LiveStoreOrderCancelInfoVO liveStorePayInfoVO);

    /**
     * 确认收货
     * @param liveStorePayInfoVO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/livestore/order/confirm/")
    ServerResponseEntity<Boolean> livestoreOrderConfirm(@RequestBody LiveStorePayInfoVO liveStorePayInfoVO);

    /**
     * 根据订单列表及支付类型获取订单支付记录
     * @param orderIds 订单ID列表
     * @param payType 支付类型
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pay/getPayInfoByOrderIdsAndPayType")
    ServerResponseEntity<GetPayInfoByOrderIdsAndPayTypeVO> getPayInfoByOrderIdsAndPayType(@RequestParam("orderIds") List<Long> orderIds, @RequestParam("payType") Integer payType);
    
    /**
     * 收钱吧订单支付成功
     * @param sqbOrderPaySuccessBO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pay/sqbPayOrderSuccess")
    ServerResponseEntity<Boolean> sqbOrderPaySuccess(@RequestBody SQBOrderPaySuccessBO sqbOrderPaySuccessBO);
    
}
