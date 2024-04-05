package com.mall4j.cloud.order.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 确认收货事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "orderReceiptTemplate")
public class OrderReceiptTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private OrderService orderService;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        Long orderId = Json.parseObject(msg.getBody(), Long.class);
        // 众所周知，确认收货之后是不能改变状态的，
        // 但是确认收货之前是可以改变状态的，如果在确认收货之前，
        // 也就是下面这条sql执行之前进行了订单状态的改变（比如退款），那会造成不可预知的后果，
        // 所以更新订单状态的时候也要在条件当中加上订单状态，确定这条sql是原子性的
        // 这里的确认收货，条件加上订单的状态，确保这次更新是幂等的
        int updateStats = orderService.receiptOrder(orderId);
        if (updateStats == 0) {
            // 如果啥都没有更新，就检查一下
//            return check(msg);
        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        Long orderId = Json.parseObject(msg.getBody(), Long.class);
        List<OrderStatusBO> orderStatus = orderService.getOrdersStatus(Collections.singletonList(orderId));
        if (!Objects.equals(orderStatus.get(0).getStatus(), OrderStatus.SUCCESS.value())) {
            return TransactionStatus.RollbackTransaction;
        }
        return TransactionStatus.CommitTransaction;
    }
}
