package com.mall4j.cloud.biz.task;

import com.mall4j.cloud.biz.service.chat.SessionFileService;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutLogService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component("SessionFileTask")
@Slf4j
@RequiredArgsConstructor
public class SessionFileTask {


    @Autowired
    private SessionTimeOutLogService logService;
    @Autowired
    private SessionFileService fileService;

    /**
     * 会话存档超时规则定时任务
     */
    @PostMapping("sessionFileSend")
    @XxlJob("sessionFileSend")
    public void sessionFileSend()  {
        Long startTime=System.currentTimeMillis();
        log.info("--start sessionFileSend");
        logService.sendMessage();
        log.info("--end sessionFileSend 耗时：{}ms",System.currentTimeMillis() - startTime);

    }

    /**
     * 获取会话同意情况定时任务
     */
    @XxlJob("singleAgree")
    public void singleAgree()  {
        Long startTime=System.currentTimeMillis();
        log.info("--start singleAgree");
        fileService.singleAgree();
        log.info("--end singleAgree 耗时：{}ms",System.currentTimeMillis() - startTime);

    }

}

