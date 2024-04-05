package com.mall4j.cloud.order.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_CANCEL_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_CANCEL_TOPIC)
public class OrderCancelConsumer implements RocketMQListener<List<Long>> {

    @Autowired
    private OrderService orderService;

    /**
     * 订单取消，将订单变成已取消状态，如果订单未支付的话
     *  首先先进行订单取消，再进行其他操作，是害怕当订单取消的服务还没处理完成的时候，其他服务查询订单没有被取消，反而失败了
     *  如原本应该判断订是否已经取消，然后还原库存的，但是此时库存服务运行较快，订单服务运行较慢，导致库存服务查订单有没有被取消时，发现订单没被取消，此时还原库存失败。
     *  因此，应该先进行订单取消，再进行其他服务的操作
     */
    @Override
    public void onMessage(List<Long> orderIds) {
        // 如果订单未支付的话，将订单设为取消状态
        orderService.cancelOrderAndGetCancelOrderIds(orderIds);
    }
}
