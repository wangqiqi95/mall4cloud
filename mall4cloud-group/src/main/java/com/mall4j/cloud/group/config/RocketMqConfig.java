package com.mall4j.cloud.group.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author FrozenWatermelon
 * @date 2021/3/30
 */
@RefreshScope
@Configuration
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate groupOrderCreateTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.GROUP_ORDER_CREATE_TOPIC);
        return template;
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate groupOrderSuccessTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.GROUP_ORDER_SUCCESS_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate groupOrderUnSuccessTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.GROUP_ORDER_UN_SUCCESS_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate groupOrderUnSuccessRefundTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.GROUP_ORDER_UN_SUCCESS_REFUND_TOPIC);
        return template;
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendGroupNotifyToUserTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant. SEND_GROUP_NOTIFY_TO_USER_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendCreateOrderTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.FIXEDPRICE_ORDER_CREATE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendMaTemplateMessage() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC);
    }

    /**
     * 问卷延时判断用户是否已经提交的消息
     * @return
     */
    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate questionnaireActivityBeginTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.QUESTIONNAIRE_ACTIVITY_BEGIN_TOP);
    }
}
