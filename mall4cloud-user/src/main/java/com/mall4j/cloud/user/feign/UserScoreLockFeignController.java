package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.api.user.feign.UserScoreLockFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserScoreLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/5/19
 */
@RestController
public class UserScoreLockFeignController implements UserScoreLockFeignClient {

    @Autowired
    private UserScoreLockService userScoreLockService;

    @Override
    public ServerResponseEntity<Void> lock(List<UserScoreLockDTO> userScoreLocks) {
        userScoreLockService.lock(userScoreLocks);
        return ServerResponseEntity.success();
    }
}
