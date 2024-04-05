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
 * @date 2021-06-05 16:58:36
 */
//@Component
//@RocketMQMessageListener(topic = RocketMqConstant.LEVEL_DOWN_COUPON_EXPIRE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.LEVEL_DOWN_COUPON_EXPIRE_TOPIC)
public class LevelDownCouponExpireConsumer implements RocketMQListener<List<BindCouponDTO>> {

    @Autowired
    private CouponUserService couponUserService;

    /**
     * 等级减低失效用户优惠券
     * @param bindCouponDTOList 用户失效优惠券
     */
    @Override
    public void onMessage(List<BindCouponDTO> bindCouponDTOList) {
        couponUserService.batchDeleteUserCoupon(bindCouponDTOList);
    }
}
