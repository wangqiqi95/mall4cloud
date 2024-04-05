package com.mall4j.cloud.seckill.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.seckill.service.SeckillOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 秒杀提交订单事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "seckillOrderCreateTemplate")
public class SeckillOrderCreateTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        ShopCartOrderMergerVO mergerOrder = Json.parseObject(msg.getBody(), ShopCartOrderMergerVO.class);
        try {
            seckillOrderService.submit(mergerOrder);
        }catch (LuckException e) {
            return TransactionStatus.RollbackTransaction;
        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        ShopCartOrderMergerVO mergerOrder = Json.parseObject(msg.getBody(), ShopCartOrderMergerVO.class);
        Long orderId = mergerOrder.getShopCartOrders().get(0).getOrderId();
        if (seckillOrderService.countByOrderId(orderId) == 0) {
            return TransactionStatus.RollbackTransaction;
        }
        return TransactionStatus.CommitTransaction;
    }
}
