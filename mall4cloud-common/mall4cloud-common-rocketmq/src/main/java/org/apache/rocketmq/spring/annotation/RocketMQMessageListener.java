package org.apache.rocketmq.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQMessageListener {

    /**
     * Consumers of the same role is required to have exactly same subscriptions and consumerGroup to correctly achieve
     * load balance. It's required and needs to be globally unique.
     *
     *
     * See <a href="http://rocketmq.apache.org/docs/core-concept/">here</a> for further discussion.
     */
    String consumerGroup();

    /**
     * Topic name.
     */
    String topic();

    /**
     * Control which message can be select. Grammar please see {@link SelectorType#TAG} and {@link SelectorType#SQL92}
     * 定制: 没有type,所以只表示:  消息标签过滤，只支持或运算，eg: "tag1 || tag2 || tag3"
     */
    String selectorExpression() default "*";


}