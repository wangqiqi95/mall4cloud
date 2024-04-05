package com.mall4j.cloud.seckill.listener;

import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SECKILL_ORDER_SUBMIT_TOPIC,consumerGroup = "GID_"+RocketMqConstant.SECKILL_ORDER_SUBMIT_TOPIC)
public class SeckillOrderSubmitConsumer implements RocketMQListener<ShopCartOrderMergerVO> {


    @Autowired
    private OnsMQTransactionTemplate seckillOrderCreateTemplate;
    /**
     * 秒杀订单的创建，异步创建
     */
    @Override
    public void onMessage(ShopCartOrderMergerVO mergerOrder) {

        ShopCartItemVO shopCartItemVO = mergerOrder.getShopCartOrders().get(0).getShopCartItemDiscounts().get(0).getShopCartItems().get(0);

        // 开启事务消息，通知订单订单服务开始创建订单
        SendResult sendResult = seckillOrderCreateTemplate.sendMessageInTransaction(mergerOrder, shopCartItemVO);

        if (sendResult == null || sendResult.getMessageId() == null) {
            return;
        }

    }
}
