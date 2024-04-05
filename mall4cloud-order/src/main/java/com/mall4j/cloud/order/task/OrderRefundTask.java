package com.mall4j.cloud.order.task;


import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 订单定时任务
 *
 * @author FrozenWatermelon
 */
@Component
public class OrderRefundTask {


    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OnsMQTemplate sendNotifyToUserTemplate;

    /**
     * 取消申请超时的订单，无论该超时订单处于任何状态
     */
    @XxlJob("cancelWhenTimeOut")
    public void cancelWhenTimeOut() {
        XxlJobHelper.log("取消申请超时的订单，无论该超时订单处于任何状态");
        // 设定时间值
        Date date = DateUtil.offsetDay(new Date(), -Constant.MAX_REFUND_APPLY_TIME);
        // 获取待处理的退款订单
        // 这里需要过滤掉视频号退单，视频号退单不做超时自动关闭的操作
        List<OrderRefundVO> orderRefundList = orderRefundService.listOrderRefundTimeOut(date);
        if (CollectionUtils.isNotEmpty(orderRefundList)) {
            orderRefundService.cancelWhenTimeOut(orderRefundList);
        }
    }

    /**
     * 退款临近超时提醒,每12小时执行发送一次的提醒
     */
    @XxlJob("pressProximityTimeOutOrder")
    public void pressProximityTimeOutOrder(){
        XxlJobHelper.log("退款临近超时提醒,每12小时执行发送一次的提醒");
        // 临时超时时间为 最大申请时间 - 12小时
        int overTime = Constant.MAX_REFUND_APPLY_TIME * 24;
        Date date = DateUtil.offsetHour(new Date(), Constant.MAX_REFUND_HOUR - overTime);
        Date overDate = DateUtil.offsetDay(new Date(), -Constant.MAX_REFUND_APPLY_TIME);
        // 获取临近超时的退款订单,大于超时时间，小于临时时间
        List<OrderRefundVO> orderRefundList = orderRefundService.listProximityRefundTimeOut(date,overDate);
        if (CollectionUtils.isNotEmpty(orderRefundList)) {
            orderRefundList = orderRefundList.stream().filter(distinctByKey(OrderRefundVO::getUserId)).collect(Collectors.toList());
            List<Long> orderIds = new ArrayList<>();
            orderRefundList.forEach(orderRefundVO -> orderIds.add(orderRefundVO.getOrderId()));
            List<SendNotifyBO> notifyBOList = orderMapper.listByOrderIds(orderIds);
            for (SendNotifyBO sendNotifyBO : notifyBOList) {
                // 消息推送-退款临近超时
                sendNotifyBO.setSendType(SendTypeEnum.REFUND_OUT_TIME.getValue());
                sendNotifyBO.setHour(Constant.MAX_REFUND_APPLY_TIME);
                sendNotifyToUserTemplate.syncSend(notifyBOList);
            }
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
