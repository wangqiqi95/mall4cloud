package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface ProductCacheNames {

    /**
     * 前缀
     */
    String PRODUCT_PREFIX = "mall4cloud_product:";

    /**
     * 用户端分类列表缓存key
     */
    String CATEGORY_INFO = PRODUCT_PREFIX + "category:info:";

    /**
     * 所有分类
     */
    String CATEGORY_ALL = PRODUCT_PREFIX + "category_all";

    /**
     * 分类导航_所有分类
     */
    String CATEGORY_NAVIGATION_ALL = PRODUCT_PREFIX + "category_navigation_all";

//    /**
//     * 用户端分类列表缓存key
//     */
//    String CATEGORY_LIST = PRODUCT_PREFIX + "category:list:";

    /**
     * 根据店铺id和上级id，获取分类列表缓存key
     */
    String CATEGORY_LIST_OF_SHOP = PRODUCT_PREFIX + "category:list_of_shop:";

    /**
     * 根据店铺id获取店铺签约的分类信息(包含签约分类的父分类)
     */
    String LIST_SIGNING_CATEGORY = PRODUCT_PREFIX + "category:list_signing_category:";

    /**
     * 根据店铺id和上级id，获取分类列表缓存key
     */
    String CATEGORY_RATE = PRODUCT_PREFIX + "category:category_rate:";

    /**
     * 分类下的属性列表缓存key
     */
    String ATTRS_BY_CATEGORY_KEY = PRODUCT_PREFIX + "attrs:category_list:";

    /**
     * 分类下的品牌列表缓存key
     */
    String BRAND_LIST_BY_CATEGORY = PRODUCT_PREFIX + "brand:list_by_category_id:";

    /**
     * 店铺签约的分类列表缓存key
     */
    String SIGNING_CATEGORY_BY_SHOP_KEY = PRODUCT_PREFIX + "category:list_by_shop_id:";

    /**
     * 购物车商品数量
     */
    String SHOP_CART_ITEM_COUNT = "shop_cart:count:";

    /**
     * spu信息缓存key
     */
    String SPU_KEY = PRODUCT_PREFIX + "spu:";

    /**
     * spu详情信息缓存key
     */
    String SPU_DETAIL_KEY = PRODUCT_PREFIX + "spu:detail:";

    /**
     * spu详情-活动信息缓存key
     */
    String SPU_ACTIVITY_KEY = PRODUCT_PREFIX + "spu:activity:";


    /**
     * 置顶品牌列表信息缓存key
     */
    String BRAND_TOP = PRODUCT_PREFIX + "brand:top_list";

    /**
     * 分组列表信息缓存key
     */
    String SPU_TAG_BY_SHOP = PRODUCT_PREFIX + "spu_tag:shop_id";


    /**
     * 商品库存/销量/评论数据缓存
     */
    String SPU_EXTENSION_KEY = PRODUCT_PREFIX + "spu_extension:";

    /**
     * 商品属性数据缓存
     */
    String SPU_ATTR_VALUE_KEY = PRODUCT_PREFIX + "spu_attr_value:";

    /**
     * 根据skuId获取sku的缓存key
     */
    String SKU_KEY = PRODUCT_PREFIX + "sku:";

    String SKU_KEY_PRICECODE = PRODUCT_PREFIX + "sku_pricecode:";

    String SAVE_STORE_SKUCODE = PRODUCT_PREFIX + "save_skustore_skucode:";

    /**
     * 根据spuId获取sku和spu_sku_attr_value里面的数据
     */
    String SKU_WITH_ATTR_LIST_KEY = PRODUCT_PREFIX + "sku:spu_sku_attr_value:";

    /**
     * 根据spuId获取sku库存列表缓存key
     */
    String SKU_STOCK_LIST_KEY = PRODUCT_PREFIX + "sku_stock_list:";

    /**
     * 订阅积分礼品到货提醒缓存数据
     */
    String SCORE_PRODUCT_LIST = PRODUCT_PREFIX + "score_product_list:";

    /**
     * 小程序商品列表
     */
    String APP_PRODUCT_LIST = PRODUCT_PREFIX + "app_product_list:";

    /**
     * 小程序推荐和导购分销商品列表
     */
    String APP_PRODUCT_COMMONPAGE = PRODUCT_PREFIX + "app_product_commonpage:";

    /**
     * 小程序商品角标
     */
    String APP_PRODUCT_TAG = PRODUCT_PREFIX + "app_product_tag:";

    /**
     * 关键词联想
     */
    String HOT_SEARCH_PREDICTION = PRODUCT_PREFIX + "hot_search_prediction:";

    /**
     * spu搜索属性筛选缓存
     */
    String ATTR_FILTER_SPU_ID = PRODUCT_PREFIX + "attr_filter_spu_id";

    /**
     * 查询筛选页关联规格属性
     */
    String FILTER_PROPERTIES_ATTR = PRODUCT_PREFIX + "filter_properties_attr";


}
