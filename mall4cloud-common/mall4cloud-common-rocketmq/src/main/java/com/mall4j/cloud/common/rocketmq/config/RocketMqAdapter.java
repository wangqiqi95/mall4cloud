package com.mall4j.cloud.common.rocketmq.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author FrozenWatermelon
 * @date 2021/3/30
 */
@RefreshScope
@Configuration
public class RocketMqAdapter {

    @Autowired
    private MqConfig mqConfig;

    public OnsMQTemplate getTemplateByTopicName(String topic) {
        Properties properties = mqConfig.mqProperties();
        //修改失败重试次数为16次。
        properties.put(PropertyKeyConst.MaxReconsumeTimes, 16);
        //设置发送超时时间，单位：毫秒。
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, ""+RocketMqConstant.TIMEOUT);

        OnsMQTemplate template = new OnsMQTemplate();
        template.setTopic(topic);
        template.setProperties(properties);

        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        template.start();
        return template;
    }

    public OnsMQTransactionTemplate getTransactionTemplateByTopicName(String topic){
        Properties properties = mqConfig.mqProperties();
        //修改失败重试次数为2次。
        properties.put(PropertyKeyConst.MaxReconsumeTimes, 2);
        //设置发送超时时间，单位：毫秒。
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, ""+RocketMqConstant.TIMEOUT);
        OnsMQTransactionTemplate template = new OnsMQTransactionTemplate();
        template.setTopic(topic);
        template.setProperties(properties);

        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        template.start();
        return template;
    }
}
