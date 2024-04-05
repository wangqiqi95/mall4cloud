package com.mall4j.cloud.payment.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2021-03-15 15:26:03
 */
@Data
public class RefundInfo extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 退款单号
     */
    private Long refundId;

	private String refundNumber;

    /**
     * 关联的支付订单id
     */
    private Long orderId;

    /**
     * 关联的支付单id
     */
    private Long payId;

	/**
	 * 用户id
	 */
	private Long userId;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 回调内容
     */
    private String callbackContent;

    /**
     * 回调时间
     */
    private Date callbackTime;

	/**
	 * 是否为未成团而退款的团购订单
	 */
	private Integer unSuccessGroupOrder;

}
