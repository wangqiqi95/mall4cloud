package com.mall4j.cloud.user.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.user.dto.AddPushTaskDTO;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
//@RocketMQMessageListener(topic = RocketMqConstant.WRAPPER_GROUP_PUSH_TASK, consumerGroup = "GID_"+RocketMqConstant.WRAPPER_GROUP_PUSH_TASK)
public class WrapperGroupPushTaskConsumer implements RocketMQListener<AddPushTaskDTO> {


    @Autowired
    private GroupPushTaskService groupPushTaskService;


    @Override
    public void onMessage(AddPushTaskDTO message) {

//
//        try {
//            groupPushTaskService.wrapperGroupPushTask(message);
//        }catch (Exception e){
//            log.error("WRAPPER PUSH TASK IS FAIL,MESSAGE IS:{}",e);
//            //创建失败保存参数
////            groupPushTaskService.createFail();
//        }


    }
}
