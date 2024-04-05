package com.mall4j.cloud.common.cache.constant;

/**
 * @author lhd
 * @date 2020/12/18
 */
public interface DiscountCacheNames {

    /**
     * 根据满减id获取当前满减详情
     */
    String DISCOUNT_BY_ID = "discount:by_id:";


    /**
     * 根据店铺id获取当前店铺的所有满减活动
     */
    String DISCOUNT_BY_SHOPID = "discount:by_shop_id:";



}
