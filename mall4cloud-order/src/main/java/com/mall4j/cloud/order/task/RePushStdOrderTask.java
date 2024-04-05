package com.mall4j.cloud.order.task;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重新推送订单到中台任务
 *
 * @luzhengxiang
 * @create 2022-05-14 4:20 PM
 **/
@Slf4j
@Component
public class RePushStdOrderTask {


    private String orderIds = "528456,528455,528454";


    @Autowired
    private OnsMQTemplate stdOrderNotifyTemplate;

    /**
     * 订单结算
     */
    @XxlJob("rePushStdOrder")
    public void settledOrder(){
        log.info("rePushStdOrder 订单重新推送中台开始>>>>>>>>>>>");

//        for (String s : orderIds.split(",")) {
//
//            Long orderId = Long.parseLong(s);
//
//            stdOrderNotifyTemplate.syncSend(orderId);
//            log.info("订单:{}，重新推送中台。",orderId);
//        }

        log.info("rePushStdOrder 订单重新推送中台结束>>>>>>>>>>>");
    }
}
