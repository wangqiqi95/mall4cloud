package com.mall4j.cloud.discount.task;

import com.mall4j.cloud.discount.service.DiscountService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
public class DiscountTask {

    @Autowired
    private DiscountService discountService;

    /**
     * 关闭已经结束的满减折活动
     */
    @XxlJob("closeDiscount")
    public void closeDiscount() {
        discountService.closeDiscountBySetStatus();
    }
}
