package com.mall4j.cloud.coupon.listener;

import com.mall4j.cloud.api.coupon.bo.CouponGiveBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.coupon.service.CouponUserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.COUPON_GIVE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.COUPON_GIVE_TOPIC)
public class CouponGiveConsumer implements RocketMQListener<CouponGiveBO> {

    @Autowired
    private CouponUserService couponUserService;


    /**
     * 赠送优惠券给用户
     * @param couponGiveBO
     */
    @Override
    public void onMessage(CouponGiveBO couponGiveBO) {
        couponUserService.giveCoupon(couponGiveBO);
    }
}
