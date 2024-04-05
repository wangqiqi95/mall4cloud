package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface OrderCacheNames {

    /**
     * 前缀
     */
    String ORDER_PREFIX = "mall4cloud_order:";

    /**
     * 确认订单信息缓存
     */
    String ORDER_CONFIRM_KEY = ORDER_PREFIX + "order:confirm";

    /**
     * 订单uuid
     */
    String ORDER_CONFIRM_UUID_KEY = ORDER_PREFIX + "order:uuid_confirm";

    /**
     * 订单留存分析
     */
    String ORDER_TRADE_RETAINED_KEY = ORDER_PREFIX + "order:trade_retained";

    /**
     * 申请退款锁
     */
    String ORDER_REFUND_LOCK_KEY = ORDER_PREFIX + "ORDER:REFUND_LOCK:";


    /**
     * 导购 业绩 缓存
     */
    String ORDER_STAFF_SALES_KEY = ORDER_PREFIX + "order:staff:";

    /**
     * 导购个人中心佣金信息
     */
    String ORDER_STAFF_GETSTAFFCOMMISSION_KEY = ORDER_PREFIX + "order:getStaffCommission:";

    /**
     * 分销榜-业绩排行榜
     */
    String ORDER_STAFF_PAGEDISTRIBUTIONRANKING_KEY = ORDER_PREFIX + "order:pageDistributionRanking:";

    /**
     * 分销榜-我的排名
     */
    String ORDER_STAFF_DISTRIBUTIONSALESRAND_KEY = ORDER_PREFIX + "order:distributionSalesRand:";

    /**
     * 门店业绩线上订单明细
     */
    String ORDER_STAFF_PAGESTOREDISTRIBUTIONORDERS_KEY = ORDER_PREFIX + "order:pageStoreDistributionOrders:";

    /**
     * 个人发展线上订单明细
     */
    String ORDER_STAFF_PAGEDEVELOPINGORDERS_KEY = ORDER_PREFIX + "order:pageDevelopingOrders:";

    /**
     * 个人业绩线上订单明细
     */
    String ORDER_STAFF_PAGEDISTRIBUTIONORDERS_KEY = ORDER_PREFIX + "order:pageDistributionOrders:";

}
