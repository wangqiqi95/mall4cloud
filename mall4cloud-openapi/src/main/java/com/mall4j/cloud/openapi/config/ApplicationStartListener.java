package com.mall4j.cloud.openapi.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 应用启动的监听器
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private final Logger logger = LogManager.getLogger(ApplicationStartListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        long startTime = System.currentTimeMillis();
        logger.info("开始注册启动任务");
        logger.info("任务启动完成，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

}
