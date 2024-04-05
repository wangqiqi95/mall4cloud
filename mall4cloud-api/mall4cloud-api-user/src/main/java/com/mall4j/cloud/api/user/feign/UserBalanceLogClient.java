package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.bo.BalancePayBO;
import com.mall4j.cloud.api.user.bo.BalanceRefundBO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 充值信息
 * @author FrozenWatermelon
 */
@FeignClient(value = "mall4cloud-user",contextId = "user-recharge")
public interface UserBalanceLogClient {

    /**
     * 获取充值金额
     * @param balanceLogId 充值记录id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userBalanceLog/getPayAmount")
    ServerResponseEntity<Long> getPayAmount(@RequestParam("balanceLogId") Long balanceLogId);

    /**
     * 获取充值记录是否已经支付
     * @param balanceLogId 充值记录id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userBalanceLog/getIsPay")
    ServerResponseEntity<Integer> getIsPay(@RequestParam("balanceLogId") Long balanceLogId);


    /**
     * 余额支付记录
     * @param balancePayBO 记录信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userBalanceLog/insertBalancePayLog")
    ServerResponseEntity<Void> insertBalancePayLog(@RequestBody BalancePayBO balancePayBO);

    /**
     * 余额退款记录 + 执行退款
     * @param balanceRefundBO 记录信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userBalanceLog/insertBalanceRefundLog")
    ServerResponseEntity<Void> doRefund(@RequestBody BalanceRefundBO balanceRefundBO);
}
