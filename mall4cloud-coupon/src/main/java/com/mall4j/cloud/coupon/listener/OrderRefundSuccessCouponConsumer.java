package com.mall4j.cloud.coupon.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.coupon.service.CouponLockService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_REFUND_SUCCESS_COUPON_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_SUCCESS_COUPON_TOPIC)
public class OrderRefundSuccessCouponConsumer implements RocketMQListener<Long> {

    @Autowired
    private CouponLockService couponLockService;


    /**
     * 订单退款，还原商家优惠券，平台券无法还原
     * @param orderId
     */
    @Override
    public void onMessage(Long orderId) {
        couponLockService.reductionCoupon(orderId);
    }
}
