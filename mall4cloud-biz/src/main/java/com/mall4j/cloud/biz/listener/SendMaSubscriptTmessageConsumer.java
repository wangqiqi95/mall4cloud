package com.mall4j.cloud.biz.listener;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageService;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发送微信小程序订阅模版消息
 *
 * @luzhengxiang
 * @create 2022-03-05 7:39 PM
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC, consumerGroup = "GID_"+RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC)
public class SendMaSubscriptTmessageConsumer implements RocketMQListener<List<WeixinMaSubscriptTmessageSendVO>> {

    @Autowired
    WeixinMaSubscriptTmessageService weixinMaSubscriptTmessageService;

    /**
     * 订单事件推送消息给用户
     */
    @Override
    public void onMessage(List<WeixinMaSubscriptTmessageSendVO> notifyList) {
        log.info("接收到微信小程序订阅模版消息 mq请求，参数为:{}", JSONObject.toJSONString(notifyList));
        weixinMaSubscriptTmessageService.sendMessageToUser(notifyList);
    }
}
