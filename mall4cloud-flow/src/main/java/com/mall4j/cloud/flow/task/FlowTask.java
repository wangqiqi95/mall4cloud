package com.mall4j.cloud.flow.task;

import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.flow.model.FlowLog;
import com.mall4j.cloud.flow.service.FlowService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 流量统计定时任务
 *
 * @author YXF
 * @date 2021-05-21
 */
@Component
public class FlowTask {

    @Autowired
    private FlowService flowService;
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 统计用户操作信息
     */
    @XxlJob("statisticalUser")
    public void statisticalUser() throws Exception {
        //更新数据再统计
        flowService.statisticalUser();
    }
    /**
     * 统计商品信息
     */
    @XxlJob("statisticalFlowData")
    public void statisticalFlowData() throws Exception {
        //更新数据再统计
        flowService.statisticalProduct();
    }
}
