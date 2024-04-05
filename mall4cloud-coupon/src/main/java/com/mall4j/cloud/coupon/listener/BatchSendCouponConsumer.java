package com.mall4j.cloud.coupon.listener;

import com.mall4j.cloud.api.coupon.bo.TCouponBatchUserBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shijing
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.BATCH_DISTRIBUTION_COUPON_TOPIC,consumerGroup = "GID_"+RocketMqConstant.BATCH_DISTRIBUTION_COUPON_TOPIC)
public class BatchSendCouponConsumer implements RocketMQListener<TCouponBatchUserBO> {

    @Resource
    private TCouponUserService tCouponUserService;


    /**
     * 批量发优惠券给用户
     * @param param
     */
    @Override
    public void onMessage(TCouponBatchUserBO param) {

    }
}
