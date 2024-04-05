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
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_COUPON_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_COUPON_TOPIC)
public class OrderNotifyCouponConsumer implements RocketMQListener<List<Long>> {

    @Resource
    private TCouponUserService tCouponUserService;


    /**
     * 订单支付成功，将优惠券标记成已使用状态
     * @param message
     */
    @Override
    public void onMessage(List<Long> message) {
        log.info("================ 接收到核销优惠券消息，参数为：{} ====================", JSONObject.toJSONString(message));

        try {
            UpdateCouponStatusDTO updateCouponStatusDTO = tCouponUserService.selectByOrderNo(message.get(0));
            if(updateCouponStatusDTO==null){
                log.error("核销优惠券异常，查询优惠券信息为空，参数：{}。",JSONObject.toJSONString(message));
                return;
            }

            tCouponUserService.updateCouponStatus(updateCouponStatusDTO);
        }catch (Exception e){
            log.error("更新优惠券状态异常，参数：{}，Exception:{} 异常信息：{}",message,e,e.getMessage());
            return;
        }

    }
}
