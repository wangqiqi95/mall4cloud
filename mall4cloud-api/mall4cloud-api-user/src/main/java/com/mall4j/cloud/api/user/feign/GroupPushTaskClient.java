package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 群发任务
 * @author Peter_Tan
 */
@FeignClient(value = "mall4cloud-user",contextId = "group-push-task")
public interface GroupPushTaskClient {

    /**
     * 获取企业微信群发任务结果进行同步
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/syncGroupMessageSendResult")
    ServerResponseEntity syncGroupMessageSendResult();

}
