package com.mall4j.cloud.coupon.listener;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.coupon.dto.UpdateCouponStatusDTO;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.COUPON_UNLOCK_TOPIC,consumerGroup ="GID_"+RocketMqConstant.COUPON_UNLOCK_TOPIC)
public class CouponUnlockConsumer implements RocketMQListener<List<Long>> {

    @Resource
    private TCouponUserService tCouponUserService;

    /**
     * 1、优惠券锁定一定时间后，如果订单支付未支付，则解锁优惠券（有可能优惠券锁定成功，订单因为异常回滚导致订单未创建）
     * 2、取消订单，直接解锁优惠券
     */
    @Override
    public void onMessage(List<Long> message) {
        log.info("================ 接收到解锁优惠券消息，参数为：{} ====================", JSONObject.toJSONString(message));
        log.info("================ 直接丢弃，参数为：{} ====================", JSONObject.toJSONString(message));

        try {
            UpdateCouponStatusDTO updateCouponStatusDTO = tCouponUserService.selectByOrderNo(message.get(0));
            if(updateCouponStatusDTO==null){
                log.error("接收到解锁优惠券消息，查询优惠券信息为空，参数：{}。",JSONObject.toJSONString(message));
                return;
            }
            updateCouponStatusDTO.setStatus(1);
            updateCouponStatusDTO.setOrderNo(null);
            updateCouponStatusDTO.setOrderAmount(null);
            updateCouponStatusDTO.setCouponAmount(null);

            tCouponUserService.updateCouponStatus(updateCouponStatusDTO);

        }catch (Exception e){
            log.error("解锁优惠券异常，参数：{}，Exception: {} 异常信息：{}",message,e,e.getMessage());
            return;
        }

    }
}
