package com.mall4j.cloud.common.rocketmq;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.apache.commons.logging.Log;
import org.slf4j.MDC;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * 来源 com.aliyun.openservices.ons.api.bean.ProducerBean
 */
public class OnsMQTemplate implements Producer {
    /**
     * 需要注入该字段，指定构造{@code Producer}实例的属性，具体支持的属性详见{@link PropertyKeyConst}
     *
     * @see com.aliyun.openservices.ons.api.bean.ProducerBean#setProperties(Properties)
     */
    private Properties properties;
    private Producer producer;

    private String topic;

    /**
     * 启动该{@code Producer}实例，建议配置为Bean的init-method
     */
    @Override
    public void start() {
        if (null == this.properties) {
            throw new ONSClientException("properties not set");
        }

        this.producer = ONSFactory.createProducer(this.properties);
        this.producer.start();
    }

    @Override
    public void updateCredential(Properties credentialProperties) {
        if (this.producer != null) {
            this.producer.updateCredential(credentialProperties);
        }
    }

    /**
     * 关闭该{@code Producer}实例，建议配置为Bean的destroy-method
     */
    @Override
    public void shutdown() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }


    @Override
    public SendResult send(Message message) {
        return this.producer.send(message);
    }

    @Override
    public void sendOneway(Message message) {
        this.producer.sendOneway(message);
    }

    @Override
    public void sendAsync(Message message, SendCallback sendCallback) {
        this.producer.sendAsync(message, sendCallback);
    }

    @Override
    public void setCallbackExecutor(final ExecutorService callbackExecutor) {
        this.producer.setCallbackExecutor(callbackExecutor);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isStarted() {
        return this.producer.isStarted();
    }

    @Override
    public boolean isClosed() {
        return this.producer.isClosed();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 同步发送
     * @param body
     * @return
     */
    public SendResult syncSend(Object body) {
        Message msg = new Message();
        msg.setTopic(this.getTopic());
        msg.setTag("*");
        msg.setKey(MDC.get("X-B3-TraceId"));
        msg.setBody(JSON.toJSONBytes(body));
        return this.send(msg);
    }

    /**
     * 同步发送 增加tag
     * @param body
     * @return
     */
    public SendResult syncSend(Object body,String tag) {
        Message msg = new Message();
        msg.setTopic(this.getTopic());
        msg.setTag(tag);
        msg.setKey(MDC.get("X-B3-TraceId"));
        msg.setBody(JSON.toJSONBytes(body));

        return this.send(msg);
    }

    /**
     * 异步发送
     * @param body
     * @param sendCallback
     */
    public void asyncSend(Object body, SendCallback sendCallback) {
        Message msg = new Message();
        msg.setTopic(this.getTopic());
        msg.setTag("*");
        msg.setBody(JSON.toJSONBytes(body));

        this.sendAsync(msg, sendCallback);
    }

    /**
     * 延时消息
     * @param body
     * @param delayLevel
     * @return
     */
    public SendResult syncSend(Object body, int delayLevel) {
        Message msg = new Message();
        msg.setTopic(this.getTopic());
        msg.setTag("*");
        msg.setBody(JSON.toJSONBytes(body));

        if (delayLevel != 0) {
            long delayTime = fetchDelayTime(delayLevel);
            msg.setStartDeliverTime(delayTime);
        }

        return this.send(msg);
    }

    public void destroy() {
        this.shutdown();
    }

    private final static long[] DELAY_LEVEL = {0L, 1000L, 5000L, 10000L, 30000L, 60000L, 120000L,
            180000L, 240000L, 300000L, 360000L, 420000L, 480000L, 540000L, 600000L, 1200000L, 1800000L, 3600000L, 7200000L};

    private long fetchDelayTime(int delayLevel) {
        return System.currentTimeMillis() + DELAY_LEVEL[delayLevel];
    }


}
