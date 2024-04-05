package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.feign.UserLevelLogClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.UserLevelLog;
import com.mall4j.cloud.user.service.UserLevelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员等级日志
 * @author FrozenWatermelon
 */
@RestController
public class UserLevelLogFeignController implements UserLevelLogClient {

    @Autowired
    private UserLevelLogService userLevelLogService;

    @Override
    public ServerResponseEntity<Long> getPayAmount(Long userLevelLogId) {
        UserLevelLog userLevelLog = userLevelLogService.getByLevelLogId(userLevelLogId);
        return ServerResponseEntity.success(userLevelLog.getPayAmount());
    }

    @Override
    public ServerResponseEntity<Integer> getIsPay(Long userLevelLogId) {
        UserLevelLog userLevelLog = userLevelLogService.getByLevelLogId(userLevelLogId);
        return ServerResponseEntity.success(userLevelLog.getIsPayed());
    }

}
