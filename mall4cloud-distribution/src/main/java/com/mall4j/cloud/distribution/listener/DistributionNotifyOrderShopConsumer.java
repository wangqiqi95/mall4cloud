package com.mall4j.cloud.distribution.listener;

import cn.hutool.core.collection.CollUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.multishop.bo.ShopWalletBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.distribution.bo.DistributionNotifyOrderAndShopBO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单支付成功，分销通知
 * @author cl
 * @date 2021-08-17 17:42:17
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_SHOP_TOPIC,consumerGroup = "GID_"+RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_SHOP_TOPIC)
public class DistributionNotifyOrderShopConsumer implements RocketMQListener<DistributionNotifyOrderAndShopBO> {

    @Autowired
    private OnsMQTemplate distributionNotifyShopTemplate;
    @Autowired
    private OnsMQTemplate distributionNotifyOrderTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(DistributionNotifyOrderShopConsumer.class);

    /**
     * 分销完成，通知订单和店铺修改订单项分销金额和店铺店铺待结算金额
     */
    @Override
    public void onMessage(DistributionNotifyOrderAndShopBO message) {
        LOG.info("分销金额计算完成后回调开始... message: " + Json.toJsonString(message));

        // 通知修改店铺钱包待结算金额 DistributionNotifyShopConsumer
        List<ShopWalletBO> shopWalletBOList = message.getShopWalletBOList();
        if (CollUtil.isNotEmpty(shopWalletBOList)) {
            // 发送消息， 通知店铺修改店铺钱包待结算金额
            SendResult sendResult = distributionNotifyShopTemplate.syncSend(shopWalletBOList);
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }

        // 通知修改订单项分销金额 DistributionNotifyOrderConsumer
        List<EsOrderItemBO> distributionOrderItems = message.getDistributionOrderItems();
        if (CollUtil.isNotEmpty(distributionOrderItems)) {
            // 发送消息， 通知修改订单项分销金额
            SendResult sendResult = distributionNotifyOrderTemplate.syncSend(distributionOrderItems);
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }
    }
}
