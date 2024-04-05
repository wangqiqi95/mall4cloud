package com.mall4j.cloud.order.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.utils.ZhlsApiUtil;
import ma.glasnost.orika.MapperFacade;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 确认收货
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_RECEIPT_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_RECEIPT_TOPIC)
public class OrderReceiptConsumer implements RocketMQListener<Long> {


    @Autowired
    private OnsMQTemplate sendNotifyToShopTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OnsMQTemplate sendNotifyToUserExtensionTemplate;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ZhlsApiUtil zhlsApiUtil;

    /**
     * 订单确认收货监听
     */
    @Override
    public void onMessage(Long orderId) {

        // todo 通知其他服务,如通知用户服务，增加成长值


        EsOrderBO esOrderBO = orderService.getEsOrder(orderId);
        if(StrUtil.isNotEmpty(esOrderBO.getTraceId())){
            liveStoreClient.orderRecieved(orderId);
        }

        List<Order> orders = new ArrayList<>();
        Order o = BeanUtil.copyProperties(esOrderBO, Order.class);
        orders.add(o);
        zhlsApiUtil.addOrder(orders,"1180");

        // 推送确认收货的通知
//        List<SendNotifyBO> notifyBOList = orderService.listByOrderIds(Collections.singletonList(orderId));
//        SendNotifyBO sendNotifyBO = notifyBOList.get(0);
//        sendNotifyBO.setSendType(SendTypeEnum.RECEIPT_ORDER.getValue());
//        SendResult sendResult = sendNotifyToShopTemplate.syncSend(sendNotifyBO);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }

        // 推送用户积分、成长值增加的通知
//        sendResult = sendNotifyToUserExtensionTemplate.syncSend(orderId);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }

    }
}
