package com.mall4j.cloud.platform.task;

import com.mall4j.cloud.platform.service.StoreRenameConfService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 更改店铺名称定时任务
 * @Author axin
 * @Date 2022-11-08 13:49
 **/
@Component
@Slf4j
public class StoreRenameTask {
    @Resource
    private StoreRenameConfService storeRenameConfService;

    @XxlJob("storeRenameTask")
    public void storeRename(){
        log.info("修改店铺名称任务开始===========");
        try {
            storeRenameConfService.updateStoreNameTask();
        } catch (Exception e) {
            log.error("修改店铺名称任务异常：",e);
        }
        log.info("修改店铺名称任务结束===========");
    }

}
