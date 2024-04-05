package com.mall4j.cloud.payment.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
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
    public OnsMQTemplate orderNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_TOPIC);
    }


    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate rechargeNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.RECHARGE_NOTIFY_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate buyVipNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.BUY_VIP_NOTIFY_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userScoreTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SCORE_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate stdOrderNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STD_TOPIC);
    }

//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public RocketMQTemplate orderRefundSuccessShopTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_SHOP_TOPIC);
//    }


}
