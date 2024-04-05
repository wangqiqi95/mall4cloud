package com.mall4j.cloud.auth.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author cl
 * @date 2021-05-14 17:58:19
 */
@RefreshScope
@Configuration
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate userNotifyRegisterTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.BATCH_USER_REGISTER_TOPIC);
        return template;
    }

}
