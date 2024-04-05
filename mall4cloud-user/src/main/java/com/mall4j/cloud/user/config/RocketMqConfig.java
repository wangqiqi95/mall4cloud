package com.mall4j.cloud.user.config;

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
    public OnsMQTemplate couponGiveTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUPON_GIVE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate balancePayTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.BALANCE_PAY_TOPIC);
        return template;
    }

    // @Lazy
    // @Bean(destroyMethod = "destroy")
    // public RocketMQTemplate userNotifyRegisterTemplate() {
    //     return rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.BATCH_USER_REGISTER_TOPIC);
    // }


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
    public OnsMQTemplate sendNotifyToUserTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_NOTIFY_TO_USER_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userScoreTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SCORE_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userNotifyDistributionUserTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.USER_NOTIFY_DISTRIBUTION_USER_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userActionSaveNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.USER_ACTION_SAVE_NOTIFY_TOPIC);
    }


    // @Lazy
    // @Bean(destroyMethod = "destroy")
    // public RocketMQTemplate userRegisterGiftTemplate() {
    //     return rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.USER_REGISTER_GIFT);
    // }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userPerfectDataTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.USER_PERFECT_DATA);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendMaSubcriptMessageTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendQiWeiMsgTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_BIZ_QI_WEI_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendUserPullNewTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.USER_PULL_NEW_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendSyncPointConvertDataTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SYNC_POINT_CONVERT_DATA);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendWrapperGroupPushTaskTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.WRAPPER_GROUP_PUSH_TASK);
    }

//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public OnsMQTemplate startGroupPushTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.START_GROUP_PUSH_TOPIC);
//    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate endGroupPushTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.END_GROUP_PUSH_TOPIC);
    }

//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public OnsMQTemplate staffGroupTaskPushTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.STAFF_GROUP_TASK_PUSH);
//    }

}
