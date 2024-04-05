package com.mall4j.cloud.order.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 退款原因
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderReturnReason extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 订单退款原因ID
     */
    private Long reasonId;

    /**
     * 退货原因名
     */
    private String reason;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 优惠总额
     */
    private Long reduceAmount;

	public Long getReasonId() {
		return reasonId;
	}

	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	@Override
	public String toString() {
		return "OrderReturnReason{" +
				"reasonId=" + reasonId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",reason=" + reason +
				",seq=" + seq +
				",status=" + status +
				",reduceAmount=" + reduceAmount +
				'}';
	}
}
