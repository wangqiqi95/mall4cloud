package com.mall4j.cloud.payment.bo;


import com.mall4j.cloud.common.constant.PayType;
import lombok.Data;

import java.util.Date;

/**
 * 退款信息
 * @author FrozenWatermelon
 */
@Data
public class RefundInfoBO {

    /**
     * 支付方式
     */
    private PayType payType;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 支付单号
     */
    private Long refundId;

    private String refundNumber;

    /**
     * 付款金额
     */
    private Long payAmount;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 通知结果
     */
    private String notifyUrl;

    /**
     * 是否直接退款
     */
    private Integer onlyRefund;

    /**
     * 外部订单流水号
     */
    private String bizPayNo;

    private Long userId;

    /**
     * 商户订单创建时间
     */
    private Date salesTime;

    /**
     * 下单门店ID
     */
    private Long storeId;

}
