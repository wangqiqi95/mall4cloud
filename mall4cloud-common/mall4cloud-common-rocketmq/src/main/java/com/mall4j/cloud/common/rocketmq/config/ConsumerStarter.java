package com.mall4j.cloud.common.rocketmq.config;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.mall4j.cloud.common.rocketmq.util.RocketMQListenerLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class ConsumerStarter {

    @Autowired
    private RocketMQListenerLoader rocketMQListenerLoader;

    @Autowired
    private MqConfig mqConfig;

    @Value("${spring.application.name}")
    private String appname;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public void buildConsumer() {

        //配置文件
        Properties properties = mqConfig.mqProperties();
        log.info("MQ ConsumerStarter start with {}", properties);

        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");


        // 查找mq消费者
        for (Map.Entry<String, RocketMQListener> stringRocketMQListenerEntry : rocketMQListenerLoader.getMap().entrySet()) {
            ConsumerBean consumerBean = new ConsumerBean();
            consumerBean.setProperties(properties);

            RocketMQListener value = stringRocketMQListenerEntry.getValue();

            MessageCommonListener messageCommonListener = new MessageCommonListener<>(value);

            Class<?> clazz = value.getClass();
            RocketMQMessageListener listenerAnnotaion = clazz.getAnnotation(RocketMQMessageListener.class);
            if (listenerAnnotaion == null) {
                log.warn("{} 找不到注释-RocketMQMessageListener", clazz);
                continue;
            }
            properties.setProperty(PropertyKeyConst.GROUP_ID, listenerAnnotaion.consumerGroup());
            //订阅关系
            Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
            Subscription subscription = new Subscription();
            subscription.setTopic(listenerAnnotaion.topic());
            subscription.setExpression(listenerAnnotaion.selectorExpression());
            subscriptionTable.put(subscription, messageCommonListener);
            //订阅多个topic如上面设置
            consumerBean.setSubscriptionTable(subscriptionTable);
            consumerBean.start();
        }



    }

}