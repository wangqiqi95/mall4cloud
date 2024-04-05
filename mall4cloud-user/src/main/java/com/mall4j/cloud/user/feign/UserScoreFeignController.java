package com.mall4j.cloud.user.feign;

import cn.hutool.core.util.ObjectUtil;
import com.mall4j.cloud.api.user.feign.UserScoreFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserExtensionService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shijing
 * @date 2022/1/28
 */
@RestController
public class UserScoreFeignController implements UserScoreFeignClient {

    @Resource
    private UserExtensionService userExtensionService;

    @Override
    public ServerResponseEntity<Void> addScore(Long userId, Long score) {
        UserExtension userExtension = userExtensionService.getByUserId(userId);
        if (ObjectUtil.isEmpty(userExtension)){
            throw new LuckException("用户扩展信息为空！");
        }
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userExtensionService.updateUserScoreOrGrowth(userIds,score,null);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> reduceScore(Long userId, Long score) {
        UserExtension userExtension = userExtensionService.getByUserId(userId);
        if (ObjectUtil.isEmpty(userExtension)){
            throw new LuckException("用户扩展信息为空！");
        }
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userExtensionService.reduceScore(userId,score);

        return ServerResponseEntity.success();
    }
}
