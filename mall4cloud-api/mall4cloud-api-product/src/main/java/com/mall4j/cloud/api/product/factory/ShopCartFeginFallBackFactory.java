package com.mall4j.cloud.api.product.factory;

import com.mall4j.cloud.api.product.feign.ShopCartFeignClient;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ShopCartFeginFallBackFactory implements FallbackFactory<ShopCartFeignClient> {
    @Override
    public ShopCartFeignClient create(Throwable cause) {
        log.error("请求购物车出错:{} {}" ,cause,cause.getMessage());
        return new ShopCartFeignClient() {
            @Override
            public ServerResponseEntity<List<ShopCartItemVO>> getCheckedShopCartItems(Long storeId) {
                return ServerResponseEntity.showFailMsg("获取购物项失败");
            }

            @Override
            public ServerResponseEntity<Void> deleteItem(List<Long> shopCartItemIds) {
                return ServerResponseEntity.showFailMsg("删除用户购物车物品失败");
            }

            @Override
            public ServerResponseEntity<Void> updateIsClosedByShopIds(List<Long> shopIds, Integer isClosed) {
                return ServerResponseEntity.showFailMsg("更新购物车失败");
            }
        };
    }
}
