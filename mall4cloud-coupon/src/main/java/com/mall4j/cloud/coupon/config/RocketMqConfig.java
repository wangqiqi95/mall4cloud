package com.mall4j.cloud.coupon.config;

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
    public OnsMQTemplate couponMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUPON_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate levelUpCouponGiveTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.LEVEL_UP_COUPON_GIVE_TOPIC);
    }

    /**
     * 原会员等级降低 取消优惠券
     * @return
     */
//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public OnsMQTemplate levelDownCouponExpireTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.LEVEL_DOWN_COUPON_EXPIRE_TOPIC);
//    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate batchSendCouponExpireTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.BATCH_DISTRIBUTION_COUPON_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendMaSubcriptMessageTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC);
    }
}
