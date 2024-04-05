package com.mall4j.cloud.order.task;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.utils.ZhlsApiUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 订单定时任务
 *
 * @author FrozenWatermelon
 */
@Component
@RefreshScope
@Slf4j
public class OrderTask {

    @Value("${mall4cloud.order.settledTaskBatchSize:20}")
    @Setter
    private Integer settledTaskBatchSize;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OnsMQTransactionTemplate orderSettledShopTemplate;

    @Autowired
    private OnsMQTransactionTemplate orderReceiptTemplate;

    @Autowired
    private ZhlsApiUtil zhlsApiUtil;

    public static void main(String[] args) {
        System.out.println(DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -15)));
    }


    /**
     * 订单结算
     */
    @XxlJob("settledOrder")
    public void settledOrder(){
        log.info("settledOrder订单结算开始>>>>>>>>>>>");
        Date now = new Date();
        // 确认收货15天的订单，进行结算
        List<Long> orderIds = orderService.listOrderId(OrderStatus.SUCCESS.value(), DateUtil.beginOfDay(DateUtil.offsetDay(now, -15)));
//        List<Long> orderIds = orderService.listDistributionOrderId(OrderStatus.SUCCESS.value(), DateUtil.beginOfMinute(DateUtil.offsetMinute(new Date(), -1)));
        List<Long> closeOrderIds = orderService.listDistributionOrderId(OrderStatus.CLOSE.value(), DateUtil.beginOfMinute(DateUtil.offsetMinute(new Date(), -1)));
        Optional.ofNullable(orderIds).orElse(new ArrayList<>()).addAll(Optional.ofNullable(closeOrderIds).orElse(new ArrayList<>()));
        if (CollectionUtil.isEmpty(orderIds)) {
            log.info("暂无结算订单");
            return;
        }
        //消息体限制，单次最多只处理结算500个订单。
        if (orderIds.size() > settledTaskBatchSize) {
            orderIds = orderIds.subList(0, settledTaskBatchSize);
        }

        log.info("结算订单数量：{}, 批次大小：{}, mq消息体：{}", orderIds.size(), settledTaskBatchSize, JSON.toJSONBytes(orderIds));
        // 发送mq消息
        // 开启事务消息，通知订单自己，开始往各个服务发送通知了
        SendResult sendResult = orderSettledShopTemplate.sendMessageInTransaction(orderIds, null);

        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        log.info("settledOrder订单结算结束>>>>>>>>>>>");
    }


    /**
     * 确认收货
     */
    @XxlJob("confirmOrder")
    public void confirmOrder(){
        Date now = new Date();
        // 订单15天的订单，进行确认收货
        // 20220608 按客户要求修改为21天
        List<Long> orderIds = orderService.confirmOrderListOrderId(OrderStatus.CONSIGNMENT.value(), DateUtil.beginOfDay(DateUtil.offsetDay(now, -21)));

        if (CollectionUtil.isEmpty(orderIds)) {
            return;
        }

        for (Long orderId : orderIds) {
            // 开启事务消息，通知订单自己，开始往各个服务发送通知了
            SendResult sendResult = orderReceiptTemplate.sendMessageInTransaction(orderId, null);

            if (sendResult == null || sendResult.getMessageId() == null) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }
    }

    /**
     * 埋点推送数据
     */
    @XxlJob("addOrderSum")
    public void addOrderSum(){
        JSONObject object = orderService.yesterdayOrderStatistics();
        zhlsApiUtil.addOrderSum(object);
    }

    /**
     * 埋点推送数据
     */
    @XxlJob("addWxAppVisit")
    public void addWxAppVisit(){
        zhlsApiUtil.addWxAppVisit();
    }
}
