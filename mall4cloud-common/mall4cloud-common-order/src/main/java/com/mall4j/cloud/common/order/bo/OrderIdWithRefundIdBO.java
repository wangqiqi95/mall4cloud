package com.mall4j.cloud.common.order.bo;

import lombok.Data;

import java.util.List;

/**
 * 订单和退款id
 * @author FrozenWatermelon
 * @date 2021/04/26
 */
@Data
public class OrderIdWithRefundIdBO {

    /**
     * 关联的退款id
     */
    private Long refundId;

    /**
     * 退款编号
     */
    private String refundNumber;

    /**
     * 关联的支付订单id
     */
    private Long orderId;

}
