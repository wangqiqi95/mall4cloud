package com.mall4j.cloud.distribution.task;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityMapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DistributionCommissionActivityTask {

    @Autowired
    private DistributionCommissionActivityMapper distributionCommissionActivityMapper;

    /**
     * 更新活动状态为失效
     */
    @XxlJob("updateActivityStatusByTime")
    public void updateActivityStatusByTime() {
        log.info("开始更新活动状态为失效任务》》》》》》》》》》》》》》》》》》》》》");
        try {
            distributionCommissionActivityMapper.updateActivityStatusByTime();
        } catch (Exception e) {
            log.error("更新活动状态为失效任务失败",e);
        }
        log.info("结束更新活动状态为失效任务》》》》》》》》》》》》》》》》》》》》》");
    }

}
