package com.mall4j.cloud.distribution.model;

import lombok.Data;

import java.util.Date;

/**
 * 订单佣金流水信息
 *
 * @TableName distribution_order_commission_log
 */
@Data
public class DistributionOrderCommissionLog {
    /**
     * id
     */
    private Long id;

    /**
     * 身份类型 1导购 2微客
     */
    private Integer identityType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户门店
     */
    private Long storeId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 佣金类型 1-分销佣金 2-发展佣金
     */
    private Integer commissionType;

    /**
     * 佣金金额
     */
    private Long commissionAmount;

    /**
     * 佣金状态1-已结算 2-已提现 3-退佣金
     */
    private Integer commissionStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}