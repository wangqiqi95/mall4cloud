package com.mall4j.cloud.common.constant;

/**
 * 分布式id key
 *
 * @author FrozenWatermelon
 * @date 2021/4/9
 */
public interface DistributedIdKey {

    /**
     * 支付单号
     */
    String MALL4CLOUD_PAY = "mall4cloud-pay";

    /**
     * 订单号
     */
    String MALL4CLOUD_ORDER = "mall4cloud-order";

    /**
     * 退款单号
     */
    String MALL4CLOUD_REFUND = "mall4cloud-refund";

    /**
     * 统一用户id uid
     */
    String MALL4CLOUD_AUTH_USER = "mall4cloud-auth-account";

    /**
     * 用户id
     */
    String MALL4CLOUD_USER = "mall4cloud-user";

    /**
     * 平台用户id
     */
    String MALL4CLOUD_PLATFORM_USER = "mall4cloud-platform-user";

    /**
     * 商家用户id
     */
    String MALL4CLOUD_MULTISHOP_USER ="mall4cloud-multishop-user";

    /**
     * 分销用户id
     */
    String MALL4CLOUD_DISTRIBUTION_USER = "mall4cloud-distribution-user";

    /**
     * 分销提现流水号
     */
    String MALL4CLOUD_DISTRIBUTION_WITHDRAW_ORDER = "mall4cloud-distribution-withdraw-order";

    /**
     * 导购用户id
     */
    String MALL4CLOUD_STAFF_USER = "mall4cloud-staff-user";

    /**
     * 导购用户id
     */
    String MALL4CLOUD_STORE_RENO = "mall4cloud-store-reno";
}
