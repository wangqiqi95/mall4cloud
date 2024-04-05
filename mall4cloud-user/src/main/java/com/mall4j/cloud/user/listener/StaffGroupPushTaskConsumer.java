package com.mall4j.cloud.user.listener;//package com.mall4j.cloud.user.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mall4j.cloud.wx.rocketmq.config.RocketMqConstant;
//import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
//import com.mall4j.cloud.user.service.GroupPushTaskService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@RocketMQMessageListener(topic = RocketMqConstant.STAFF_GROUP_TASK_PUSH,consumerGroup = "GID_"+RocketMqConstant.STAFF_GROUP_TASK_PUSH)
//public class StaffGroupPushTaskConsumer implements RocketMQListener<StartGroupPushDTO> {
//
//    @Autowired
//    private GroupPushTaskService groupPushTaskService;
//
//    /**
//     * 导购端群发任务触发，进行发送
//     * 首次发送：借助企微群发能力，触达人群中已加好友的一次全部发送，每个企微外部好友每天仅收到一条，完成触达后提示【已完成，本次预计触达xx人】；
//     * 按钮可以重复点击：此子任务中已触达的人群不再重复触达，完成触达后提示【已触达客户不会重复触达，本次预计触达xx人】；
//     * 如判断没有可以触达的友好：报错提示【当前无可触达的用户】；
//     *
//     * @param message
//     */
//    @Override
//    public void onMessage(StartGroupPushDTO message) {
//        log.info("进入群发任务触发异步任务中,当前入参：{}", JSONObject.toJSONString(message));
//        groupPushTaskService.startCpGroupPush(message);
//    }
//}