package com.mall4j.cloud.biz.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RocketMqConfig {

    private final  RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate staffSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.STAFF_SYNC_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate qiWeiFriendsSyncTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.QI_WEI_FRIENDS_SYNC_NOTIFY_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate soldUploadExcelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SOLD_UPLOAD_EXCEL);
    }

}
