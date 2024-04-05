package com.mall4j.cloud.product.task;

import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.TagActivityService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 营销标签活动定时任务
 *
 * @author hwy
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TagActivityTask {

    private final TagActivityService tagActivityService;

    /**
     * 检测活动的开始时间和结束时间，将状态更新
     *
     */
    @XxlJob("updateTagActivityStatusTask")
    public void updateTagActivityStatusTask(){
        tagActivityService.updateTagActivityStatus();
    }
}
