package com.mall4j.cloud.user.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RocketMQMessageListener(topic = RocketMqConstant.END_GROUP_PUSH_TOPIC,consumerGroup = "GID_"+RocketMqConstant.END_GROUP_PUSH_TOPIC)
public class EndGroupPushConsumer implements RocketMQListener<List<Long>> {

    @Autowired
    private StaffBatchSendCpMsgService staffBatchSendCpMsgService;


    @Override
    public void onMessage(List<Long> sonTaskIdList) {
        staffBatchSendCpMsgService.cancelExternalContactCpMsg(sonTaskIdList);
    }
}
