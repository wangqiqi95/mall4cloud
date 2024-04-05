package com.mall4j.cloud.product.task;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.product.service.SpuService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 商品定时任务
 *
 * @author FrozenWatermelon
 */
@Component
public class ProductTask {
    @Autowired
    private SpuService spuService;

    /**
     * 校验商品数量是否完整
     * 商品数据是否为最新的：根据商品更新时间判断
     */
    @XxlJob("verifySpuData")
    public void verifySpuData(){
        spuService.verifySpuData();
    }
}
