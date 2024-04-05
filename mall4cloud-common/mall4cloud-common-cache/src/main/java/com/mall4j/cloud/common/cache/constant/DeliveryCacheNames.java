package com.mall4j.cloud.common.cache.constant;

/**
 * @author lhd
 * @date 2020/12/08
 */
public interface DeliveryCacheNames {

    /**
     * 前缀
     */
    String DELIVERY_PREFIX = "mall4cloud_delivery:";

    /**
     * 根据运费模板id获取运费模板的缓存key
     */
    String TRANSPORT_BY_ID_PREFIX = DELIVERY_PREFIX + "transport:by_id:";

    /**
     * 根据运费模板id获取运费模板的缓存key
     */
    String SAME_CITY_BY_ID_PREFIX = DELIVERY_PREFIX + "same_city:by_id:";


    /**
     * 店铺分类列表缓存key
     */
    String AREA_KEY = DELIVERY_PREFIX + "area";

    /**
     * 店铺分类列表缓存key
     */
    String AREA_INFO_KEY = DELIVERY_PREFIX + "area_info";

}
