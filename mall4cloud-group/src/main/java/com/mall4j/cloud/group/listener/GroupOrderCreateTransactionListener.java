package com.mall4j.cloud.group.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.group.bo.GroupOrderBO;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 * 确认收货事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "groupOrderCreateTemplate")
public class GroupOrderCreateTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        ServerResponseEntity<List<Long>> submitResponse = orderFeignClient.submit((ShopCartOrderMergerVO) arg);
        if (!submitResponse.isSuccess()) {
            return check(msg);
        }

        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        GroupOrderBO groupOrderBO = Json.parseObject(msg.getBody(), GroupOrderBO.class);
        Long orderId = groupOrderBO.getOrderId();
        ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(Collections.singletonList(orderId));

        if (!ordersStatusResponse.isSuccess()) {
            return TransactionStatus.Unknow;
        }
        List<OrderStatusBO> data = ordersStatusResponse.getData();
        // 如果订单状态没有，也就是没有创建成功订单，直接回滚
        if (data.get(0).getStatus() == null) {
            return TransactionStatus.RollbackTransaction;
        }
        return TransactionStatus.CommitTransaction;
    }
}
