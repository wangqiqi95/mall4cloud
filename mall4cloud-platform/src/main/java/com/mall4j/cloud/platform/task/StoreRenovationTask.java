package com.mall4j.cloud.platform.task;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.platform.service.StoreRenovationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 */
@RestController
@Component("StoreRenovationTask")
@Slf4j
public class StoreRenovationTask {

    @Autowired
    private StoreRenovationService storeRenovationService;

    /**
     * 定时发布
     */
    @PostMapping("pushStoreRenovationTask")
    @XxlJob("pushStoreRenovationTask")
    public void pushStoreRenovationTask()  {

        Long startTime=System.currentTimeMillis();
        log.info("--start pushStoreRenovationTask");
        storeRenovationService.pushStoreRenovation();
        log.info("--end pushStoreRenovationTask 耗时：{}ms",System.currentTimeMillis() - startTime);

    }

    public static void main(String[] strings){
        System.out.println(DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH"+":00:00"),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH"+":00:00"),"yyyy-MM-dd HH:mm:ss").getTime());
    }

}

