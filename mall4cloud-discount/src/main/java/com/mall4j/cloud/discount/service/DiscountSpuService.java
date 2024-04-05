package com.mall4j.cloud.discount.service;

import java.util.List;

/**
 * 满减满折商品关联表
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public interface DiscountSpuService {

    /**
     * 获取关联的商品列表
     * @param discountId
     * @return
     */
    List<Long> listSpuIdByDiscountId(Long discountId);
}
