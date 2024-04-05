package com.mall4j.cloud.common.rocketmq.config;

import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.mall4j.cloud.common.rocketmq.util.RocketMQTemplateLoader;
import com.mall4j.cloud.common.rocketmq.util.RocketMQTransactionLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Slf4j
public class TransactionStarter {

    @Autowired
    private RocketMQTransactionLoader rocketMQTransactionLoader;

    @Autowired
    private RocketMQTemplateLoader rocketMQTemplateLoader;

    @Bean
    public void buildTransaction() {
        // 查找mq消费者
        for (Map.Entry<String, LocalTransactionChecker> stringRocketMQListenerEntry : rocketMQTransactionLoader.getMap().entrySet()) {
            LocalTransactionChecker value = stringRocketMQListenerEntry.getValue();
            Class<?> clazz = value.getClass();
            RocketMQTransactionListener transactionListener = clazz.getAnnotation(RocketMQTransactionListener.class);
            rocketMQTemplateLoader.getMap().get(transactionListener.rocketMQTemplateBeanName()).setLocalTransactionChecker(value);
        }

    }

}