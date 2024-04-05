package com.mall4j.cloud.transfer.task;

import com.mall4j.cloud.transfer.service.OrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @luzhengxiang
 * @create 2022-04-21 9:12 PM
 **/
@Component
public class OrderTransferTask {

    @Autowired
    OrderService orderService;

    @XxlJob("orderTransfer")
    public void order(){
        orderService.orderTransfer();
    }
}
