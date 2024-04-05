package org.apache.rocketmq.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQTransactionListener {

    String rocketMQTemplateBeanName();

}