package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
@FeignClient(value = "mall4cloud-user",contextId = "userScoreLock")
public interface UserScoreLockFeignClient {

    /**
     * 锁定积分
     * @param userScoreLocks 参数
     * @return 是否成功
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userScore/lock")
    ServerResponseEntity<Void> lock(@RequestBody List<UserScoreLockDTO> userScoreLocks);

}
