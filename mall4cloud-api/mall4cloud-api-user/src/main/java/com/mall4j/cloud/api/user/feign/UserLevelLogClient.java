package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.bo.BalancePayBO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 会员等级日志
 * @author FrozenWatermelon
 */
@FeignClient(value = "mall4cloud-user",contextId = "user-level")
public interface UserLevelLogClient {

    /**
     * 获取会员等级购买金额，并生成购买vip的订单
     * @param userLevelLogId 会员等级购买记录id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userLevel/getPayAmount")
    ServerResponseEntity<Long> getPayAmount(@RequestParam("userLevelLogId") Long userLevelLogId);

    /**
     * 获取会员等级购买记录是否已经支付
     * @param userLevelLogId 会员等级购买记录id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userLevel/getIsPay")
    ServerResponseEntity<Integer> getIsPay(@RequestParam("userLevelLogId") Long userLevelLogId);


}
