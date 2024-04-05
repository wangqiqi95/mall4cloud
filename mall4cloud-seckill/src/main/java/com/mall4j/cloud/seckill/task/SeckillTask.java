package com.mall4j.cloud.seckill.task;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.seckill.model.Seckill;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 秒杀订单的定时任务
 * @author FrozenWatermelon
 */
@Component
public class SeckillTask {

    @Autowired
    private SeckillService seckillService;


    /**
     * 秒杀结束，改变商品类型
     */
    @XxlJob("activityFinishAndProdChange")
    public void activityFinishAndProdChange(){
        XxlJobHelper.log("秒杀活动结束，秒杀商品恢复为普通商品。。。");
        // 获取活动结束，需要改变商品类型的列表

        List<Seckill> seckillList = seckillService.listUnEndButNeedEndActivity();
        if (CollectionUtil.isEmpty(seckillList)) {
            return;
        }
        seckillService.changeProdTypeBySeckillIdList(seckillList);

    }
}
