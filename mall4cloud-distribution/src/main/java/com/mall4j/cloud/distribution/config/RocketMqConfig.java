package com.mall4j.cloud.distribution.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author cl
 * @date 2021/8/5
 */
@RefreshScope
@Configuration
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate distributionNotifyOrderShopTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_SHOP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate distributionNotifyOrderTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate distributionNotifyShopTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.DISTRIBUTION_NOTIFY_SHOP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendDistributionActivityShareTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUNT_DISTRIBUTION_ACTIVITY_SHARE);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendDistributionGuideShareTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUNT_DISTRIBUTION_GUIDE_SHARE);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendDistributionUnreadRecordTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUNT_DISTRIBUTION_UNREAD_RECORD);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendMaSubcriptMessageTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate soldUploadExcelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SOLD_UPLOAD_EXCEL);
    }
}
