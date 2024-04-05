package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 联营分佣申请订单
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:48
 */
@Data
public class DistributionJointVentureCommissionApplyRefundOrder extends BaseModel {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 联营分佣申请ID
     */
    private Long applyId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 退单ID
     */
    private Long refundId;
}