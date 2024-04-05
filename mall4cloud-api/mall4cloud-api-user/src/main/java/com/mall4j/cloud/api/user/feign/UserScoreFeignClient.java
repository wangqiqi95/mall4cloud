package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author shijing
 * @date 2022/1/28
 */
@FeignClient(value = "mall4cloud-user",contextId = "userScore")
public interface UserScoreFeignClient {

    /**
     * 增加积分
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userScore/addScore")
    ServerResponseEntity<Void> addScore(@RequestParam("userId") Long userId, @RequestParam("score") Long score);

    /**
     * 扣减积分
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userScore/reduceScore")
    ServerResponseEntity<Void> reduceScore(@RequestParam("userId") Long userId, @RequestParam("score") Long score);

}
