package com.mall4j.cloud.payment.listener;

import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.payment.service.RefundInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 退款的通知的监听
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_REFUND_PAYMENT_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_PAYMENT_TOPIC)
public class OrderRefundPaymentConsumer implements RocketMQListener<PayRefundBO> {

    @Autowired
    private RefundInfoService refundInfoService;

    /**
     * 订单退款 支付服务，开始进行退款啦
     */
    @Override
    public void onMessage(PayRefundBO payRefundBO) {
        refundInfoService.doRefund(payRefundBO);
    }
}
