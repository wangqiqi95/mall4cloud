package com.mall4j.cloud.product.config;

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
    public OnsMQTemplate stockMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.STOCK_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate soldUploadExcelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SOLD_UPLOAD_EXCEL);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate aliElectronicSignTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ALI_ELECTRONIC_SIGN_SYNC_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate productSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate scoreProductAsyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SCORE_PRODUCT_ARRIVAL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate ecZeroSetProductStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.EC_ZERO_SET_PRODUCT_STOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate syncZhlsProductTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC);
    }
}
