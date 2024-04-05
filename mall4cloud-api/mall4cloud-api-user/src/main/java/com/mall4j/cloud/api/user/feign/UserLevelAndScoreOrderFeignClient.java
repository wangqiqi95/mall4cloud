package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户等级积分订单计算feign连接
 * @author FrozenWatermelon
 * @date 2021/05/10
 */
@FeignClient(value = "mall4cloud-user",contextId = "user-level-and-score")
public interface UserLevelAndScoreOrderFeignClient {


    /**
     * 计算订单等级和积分优惠
     * @param shopCartOrderMerger 订单信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userLevel/calculateLevelAndScoreDiscount")
    ServerResponseEntity<ShopCartOrderMergerVO> calculateLevelAndScoreDiscount(@RequestBody ShopCartOrderMergerVO shopCartOrderMerger);


    /**
     * 计算订单等级和积分优惠
     * @param shopCartOrderMerger 订单信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userLevel/staffCalculateLevelAndScoreDiscount")
    ServerResponseEntity<ShopCartOrderMergerVO> staffCalculateLevelAndScoreDiscount(@RequestBody ShopCartOrderMergerVO shopCartOrderMerger);

}
