package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.dto.*;
import com.mall4j.cloud.api.user.feign.GroupPushTaskClient;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 用户地址feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Slf4j
@RestController
public class GroupPushTaskController implements GroupPushTaskClient {

    @Autowired
    GroupPushSonTaskService groupPushSonTaskService;

    @Autowired
    GroupPushTaskService groupPushTaskService;

    /**
     * 获取企业微信群发任务结果进行同步
     * @return
     */
    @Override
    public ServerResponseEntity syncGroupMessageSendResult() {
        return groupPushSonTaskService.syncGroupMessageSendResult();
    }

}
