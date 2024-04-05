package com.mall4j.cloud.payment.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.order.bo.OrderIdWithRefundIdBO;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.service.RefundInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拼团订单成团失败通知
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.GROUP_ORDER_UN_SUCCESS_REFUND_TOPIC,consumerGroup = "GID_"+RocketMqConstant.GROUP_ORDER_UN_SUCCESS_REFUND_TOPIC)
public class GroupOrderUnSuccessRefundConsumer implements RocketMQListener<List<OrderIdWithRefundIdBO>> {

    @Autowired
    private RefundInfoService refundInfoService;
    @Autowired
    private PayInfoService payInfoService;

    /**
     * 拼团订单成团失败通知
     */
    @Override
    public void onMessage(List<OrderIdWithRefundIdBO> orderIdWithRefundIds) {
        log.info("拼团失败，执行退款操作，参数信息：{}", JSONObject.toJSONString(orderIdWithRefundIds));
        List<Long> orderIds = orderIdWithRefundIds.stream().map(OrderIdWithRefundIdBO::getOrderId).collect(Collectors.toList());

        List<PayInfo> payInfos = payInfoService.listByOrderIds(orderIds);
        if (CollectionUtil.isEmpty(payInfos)) {
            return;
        }
        Map<Long, OrderIdWithRefundIdBO> orderIdWithRefundIdMap = orderIdWithRefundIds.stream().collect(Collectors.toMap(OrderIdWithRefundIdBO::getOrderId,o->o));
        for (PayInfo payInfo : payInfos) {
            PayRefundBO payRefundBO = new PayRefundBO();
            payRefundBO.setRefundId(orderIdWithRefundIdMap.get(Long.valueOf(payInfo.getOrderIds())).getRefundId());
            payRefundBO.setRefundNumber(orderIdWithRefundIdMap.get(Long.valueOf(payInfo.getOrderIds())).getRefundNumber());
            payRefundBO.setRefundAmount(payInfo.getPayAmount());
            payRefundBO.setOrderId(Long.valueOf(payInfo.getOrderIds()));
            payRefundBO.setPayId(payInfo.getPayId());
            payRefundBO.setUnSuccessGroupOrder(1);
            refundInfoService.doRefund(payRefundBO);
        }
    }
}
