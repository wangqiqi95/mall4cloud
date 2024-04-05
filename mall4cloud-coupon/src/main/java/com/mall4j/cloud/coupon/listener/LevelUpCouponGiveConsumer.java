package com.mall4j.cloud.coupon.listener;

import com.mall4j.cloud.api.coupon.dto.BindCouponDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.coupon.service.CouponUserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cl
 * @date 2021-05-17 14:58:36
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.LEVEL_UP_COUPON_GIVE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.LEVEL_UP_COUPON_GIVE_TOPIC)
public class LevelUpCouponGiveConsumer implements RocketMQListener<List<BindCouponDTO>> {

    @Autowired
    private CouponUserService couponUserService;

    /**
     * 等级提升发送优惠券给客户
     * @param bindCouponDTOList 用户绑定优惠券
     */
    @Override
    public void onMessage(List<BindCouponDTO> bindCouponDTOList) {
        couponUserService.batchBindCoupon(bindCouponDTOList);
    }
}
