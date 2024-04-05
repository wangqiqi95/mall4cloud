package com.mall4j.cloud.payment.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.manager.PayNoticeManager;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 订单余额支付成功监听
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.BALANCE_PAY_TOPIC,consumerGroup = "GID_"+RocketMqConstant.BALANCE_PAY_TOPIC)
public class BalancePayConsumer implements RocketMQListener<Long> {

    @Autowired
    private PayInfoService payInfoService;
    @Autowired
    private PayNoticeManager payNoticeManager;
    /**
     * 订单退款 支付服务，开始进行退款啦
     */
    @Override
    public void onMessage(Long payId) {
        PayInfo payInfo = payInfoService.getByPayId(payId);
        // 已经支付
        if (Objects.equals(payInfo.getPayStatus(), PayStatus.PAYED.value()) || Objects.equals(payInfo.getPayStatus(), PayStatus.REFUND.value())) {
            return;
        }
        PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
        payInfoResultBO.setPaySuccess(true);
        payInfoResultBO.setPayId(payId);
        payInfoResultBO.setCallbackContent("余额支付成功");
        payInfoResultBO.setPayAmount(payInfo.getPayAmount());
        payInfoResultBO.setBizPayNo(payId.toString());

        if (Objects.equals(payInfo.getPayEntry(), PayEntry.ORDER.value())) {
            // 订单支付回调
            payNoticeManager.noticeOrder(payInfoResultBO, payInfo);
        } else if (Objects.equals(payInfo.getPayEntry(), PayEntry.VIP.value())) {
            // 购买会员回调
            payNoticeManager.noticeBuyVip(payInfoResultBO, payInfo);
        }
    }
}
