package com.mall4j.cloud.common.rocketmq.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author liutuo
 * @date 2022-03-23
 */
@RefreshScope
@Configuration
public class MqConfig {

    @Value("${rocketmq.access-key:LTAI5tDciXuBpQnDnCX9pro7}")
    private String accessKey;

    @Value("${rocketmq.secret-key:37qILjoqFnifos3VlhCmPRCzxokTHW}")
    private String secretKey;

    @Value("${rocketmq.name-server:http://MQ_INST_1216401893930961_BYAVCMRx.mq-internet-access.mq-internet.aliyuncs.com:80}")
    private String nameServer;

    @Bean
    public Properties mqProperties() {
        Properties properties = new Properties();
        // AccessKeyId 阿里云身份验证，在阿里云用户信息管理控制台获取。
        properties.put(PropertyKeyConst.AccessKey,accessKey);
        // AccessKeySecret 阿里云身份验证，在阿里云用户信息管理控制台获取。
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        // 设置TCP接入域名，进入消息队列RocketMQ版控制台实例详情页面的接入点区域查看。
        properties.put(PropertyKeyConst.NAMESRV_ADDR, nameServer);
        return properties;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
}
