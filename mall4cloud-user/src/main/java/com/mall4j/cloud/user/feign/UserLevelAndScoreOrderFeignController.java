package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.feign.UserLevelAndScoreOrderFeignClient;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.manager.UserLevelOrderManager;
import com.mall4j.cloud.user.manager.UserScoreOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 用户等级积分订单计算feign连接
 * @author FrozenWatermelon
 * @date 2021/05/10
 */
@RestController
public class UserLevelAndScoreOrderFeignController implements UserLevelAndScoreOrderFeignClient {


    @Autowired
    private UserScoreOrderManager userScoreOrderManager;

    @Autowired
    private UserLevelOrderManager userLevelOrderManager;


    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> calculateLevelAndScoreDiscount(ShopCartOrderMergerVO shopCartOrderMerger) {
        userLevelOrderManager.calculateLevelDiscount(shopCartOrderMerger);
        userScoreOrderManager.calculateScoreDiscount(shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> staffCalculateLevelAndScoreDiscount(ShopCartOrderMergerVO shopCartOrderMerger) {
        userLevelOrderManager.calculateLevelDiscount(shopCartOrderMerger);
        userScoreOrderManager.calculateScoreDiscount(shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }


}
