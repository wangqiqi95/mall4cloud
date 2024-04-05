package com.mall4j.cloud.platform.config;

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
//@Configuration
@RequiredArgsConstructor
public class RocketMqConfig {

//    private final  RocketMqAdapter rocketMqAdapter;
//
//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public OnsMQTemplate soldUploadExcelTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SOLD_UPLOAD_EXCEL);
//    }
//
//    @Lazy
//    @Bean(destroyMethod = "destroy")
//    public OnsMQTemplate aliElectronicSignTemplate() {
//        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ALI_ELECTRONIC_SIGN_SYNC_MESSAGE_TOPIC);
//    }

}
