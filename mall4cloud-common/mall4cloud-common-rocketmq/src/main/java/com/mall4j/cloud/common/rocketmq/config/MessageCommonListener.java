package com.mall4j.cloud.common.rocketmq.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.MDC;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class MessageCommonListener<T> implements MessageListener {

    private RocketMQListener<T> myRocketMQListener;

    MessageCommonListener(RocketMQListener<T> rocketMQListener) {
        this.myRocketMQListener = rocketMQListener;
    }

    /**
     * 消费消息接口，由应用来实现<br>
     * 网络抖动等不稳定的情形可能会带来消息重复，对重复消息敏感的业务可对消息做幂等处理
     *
     * @param message 消息
     * @param context 消费上下文
     * @return 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     * @see <a href="https://help.aliyun.com/document_detail/44397.html">如何做到消费幂等</a>
     */
    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("receive message:{}, context:{}", message, context);
        if (message.getBody() == null) {
            log.error("MessageCommonListener.consume error, message=" + JSON.toJSONString(message));
            return Action.ReconsumeLater;
        }

        String body = new String(message.getBody());
        log.info("messeage.body={}", body);
        log.info("messeage.traceId={}", message.getKey());

        try {
            ParameterizedType parameterizedType = (ParameterizedType) myRocketMQListener.getClass().getGenericInterfaces()[0];
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type type = actualTypeArguments[0];

            // 解析message
            Object t = JSON.parseObject(body, type);

            //链路跟踪traceId赋值
            MDC.put("X-B3-TraceId", StrUtil.isNotBlank(message.getKey()) ? message.getKey() : "");

            if (t == null) {
                throw new LuckException("message consume error, 无法解析消息体");
            }

            // 执行listener
            myRocketMQListener.onMessage((T)t);
        } catch (Exception e) {
            log.error("message consume error", e);
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }
}
