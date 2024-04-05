package org.apache.rocketmq.spring.core;

public interface RocketMQListener<T> {
    void onMessage(T message);
}