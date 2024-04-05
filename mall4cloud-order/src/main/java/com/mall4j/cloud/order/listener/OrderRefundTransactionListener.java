package com.mall4j.cloud.order.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单退款事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "orderRefundTemplate")
public class OrderRefundTransactionListener implements LocalTransactionExecuter,LocalTransactionChecker {

    @Autowired
    private OrderRefundService orderRefundService;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        OrderRefundVO orderRefundVO = Json.parseObject(msg.getBody(), OrderRefundVO.class);

        OrderRefundDTO orderRefundParam = (OrderRefundDTO)arg;
        // 同意退款
        int updateStats = orderRefundService.agreeRefund(orderRefundVO,orderRefundParam);

        if (updateStats == 0) {
            // 如果啥都没有更新，就检查一下
            return check(msg);
        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        OrderRefundVO orderRefundVO = Json.parseObject(msg.getBody(), OrderRefundVO.class);
        OrderRefundVO dbRefundStatus = orderRefundService.getByRefundId(orderRefundVO.getRefundId());
        //退款失败，手动执行更新订单状态为买家申请，这里注释掉，重新执行。
//        if (!Objects.equals(dbRefundStatus.getReturnMoneySts(), ReturnProcessStatusEnum.PROCESSING.value())) {
//            return TransactionStatus.RollbackTransaction;
//        }
        return TransactionStatus.CommitTransaction;
    }




}
