package com.mall4j.cloud.user.listener;//package com.mall4j.cloud.user.listener;
//
//
//import com.mall4j.cloud.wx.exception.LuckException;
//import com.mall4j.cloud.wx.rocketmq.config.RocketMqConstant;
//import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
//import com.mall4j.cloud.user.manager.StaffBatchSendCpMsgManager;
//import com.mall4j.cloud.user.service.GroupPushTaskService;
//import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RocketMQMessageListener(topic = RocketMqConstant.START_GROUP_PUSH_TOPIC,consumerGroup = "GID_"+RocketMqConstant.START_GROUP_PUSH_TOPIC)
//public class StartGroupPushConsumer implements RocketMQListener<StartGroupPushDTO> {
//
//    @Autowired
//    private GroupPushTaskService groupPushTaskService;
//    @Autowired
//    private StaffBatchSendCpMsgManager staffBatchSendCpMsgManager;
//
//    private final static String FAIL = "FAIL";
//
//    @Override
//    public void onMessage(StartGroupPushDTO message) {
//        try {
//            groupPushTaskService.startCpGroupPush(message);
//        }catch (Exception e){
//            StaffBatchSendCpMsgVO msgVO = staffBatchSendCpMsgManager.getBySonTaskIdAndStaffId(message.getSonTaskId(), message.getStaffId());
//            staffBatchSendCpMsgManager.updateMsgIdById(msgVO.getStaffBatchSendCpMsgId(), FAIL);
//            log.error("CREATE CP PUSH TASK IS FAIL,MESSAGE IS:{}",e);
//            //抛出异常，重新消费
//            throw new LuckException(e.getMessage());
//        }
//
//    }
//}
