package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface CouponCacheNames {

    /**
     * 前缀
     */
    String COUPON_PREFIX = "mall4cloud_coupon:";

    /**
     * 店铺优惠券列表缓存key
     */
    String COUPON_LIST_BY_SHOP_KEY = COUPON_PREFIX + "coupon_list_by_shop:";

    /**
     *优惠券及关联商品列表缓存key
     */
    String COUPON_AND_SPU_DATA = COUPON_PREFIX + "coupon_by_coupon_id:";

    /**
     * CRM优惠券 缓存key
     */
    String CRM_COUPON_INFO = COUPON_PREFIX + "crm:info:";

    /**
     * 优惠券 缓存key
     */
    String COUPON_INFO = COUPON_PREFIX + "info:";
}
