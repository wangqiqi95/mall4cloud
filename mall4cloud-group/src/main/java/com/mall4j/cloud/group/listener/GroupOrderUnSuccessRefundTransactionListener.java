package com.mall4j.cloud.group.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.group.mapper.GroupTeamMapper;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单退款事务监听
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "groupOrderUnSuccessRefundTemplate")
public class GroupOrderUnSuccessRefundTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private GroupTeamMapper groupTeamMapper;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        int updateStatus = groupTeamMapper.updateToUnSuccess((Long)arg);
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        // 直接回滚，防止异步出现aba的问题
        // 第二个原因是因为上面那个事务消息会重复发送多次，因为GROUP_ORDER_UN_SUCCESS_TOPIC 这个会回调多次
        return TransactionStatus.RollbackTransaction;
    }

}
