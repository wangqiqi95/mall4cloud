package com.mall4j.cloud.common.rocketmq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.order.OrderProducer;

import java.util.Properties;

public class OnsMQOrderTemplate implements OrderProducer {
    /**
     * 需要注入该字段，指定构造{@code OrderProducer}实例的属性，具体支持的属性详见{@link PropertyKeyConst}
     *
     * @see com.aliyun.openservices.ons.api.bean.OrderProducerBean#setProperties(Properties)
     */
    private Properties properties;

    private OrderProducer orderProducer;

    private String topic;

    /**
     * 启动该{@code OrderProducer}实例，建议配置为Bean的init-method
     */
    @Override
    public void start() {
        if (null == this.properties) {
            throw new ONSClientException("properties not set");
        }

        this.orderProducer = ONSFactory.createOrderProducer(this.properties);
        this.orderProducer.start();
    }

    @Override
    public void updateCredential(Properties credentialProperties) {
        if (this.orderProducer != null) {
            this.orderProducer.updateCredential(credentialProperties);
        }
    }

    /**
     * 关闭该{@code OrderProducer}实例，建议配置为Bean的destroy-method
     */
    @Override
    public void shutdown() {
        if (this.orderProducer != null) {
            this.orderProducer.shutdown();
        }
    }

    @Override
    public boolean isStarted() {
        return this.orderProducer.isStarted();
    }

    @Override
    public boolean isClosed() {
        return this.orderProducer.isClosed();
    }

    @Override
    public SendResult send(final Message message, final String shardingKey) {
        return this.orderProducer.send(message, shardingKey);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void destroy() {
        this.shutdown();
    }
}
