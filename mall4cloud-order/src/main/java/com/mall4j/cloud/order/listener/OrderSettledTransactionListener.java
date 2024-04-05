package com.mall4j.cloud.order.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 确认收货事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "orderSettledShopTemplate")
public class OrderSettledTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private OrderService orderService;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        List<Long> orderIds = Json.parseArray(msg.getBody(), Long[].class);
        // 众所周知，结算之后是不能改变状态的
        int updateStats = orderService.settledOrder(orderIds);
        // 2022-08-13 部分订单该时间被设置，导致消息发送不出去，直接屏蔽该判断
//        if (updateStats == 0) {
//            // 如果啥都没有更新，就检查一下
//            return check(msg);
//        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        // 2022-08-13 部分订单该时间被设置，导致消息发送不出去，直接屏蔽该判断
//        Date now = new Date();
//        // 确认收货15天的订单，进行结算
//        List<Long> orderIds = orderService.listOrderId(OrderStatus.SUCCESS.value(), DateUtil.beginOfDay(DateUtil.offsetDay(now, -15)));
//
//        if (CollectionUtil.isEmpty(orderIds)) {
            return TransactionStatus.CommitTransaction;
//        }
//        return TransactionStatus.RollbackTransaction;
    }
}
