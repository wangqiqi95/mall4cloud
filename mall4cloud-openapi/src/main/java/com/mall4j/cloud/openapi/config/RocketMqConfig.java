package com.mall4j.cloud.openapi.config;

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
    public OnsMQTemplate productSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC);
    }

	@Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate productPriceSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ERP_PRODUCT_PRICE_SYNC_MESSAGE_TOPIC);
    }

	@Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate productStockSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ERP_PRODUCT_STOCK_SYNC_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate stdOrderNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STD_TOPIC);
    }

}
