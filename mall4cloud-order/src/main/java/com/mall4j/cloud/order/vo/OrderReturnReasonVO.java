package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退款原因VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderReturnReasonVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单退款原因ID")
    private Long reasonId;

    @ApiModelProperty("退货原因名")
    private String reason;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("启用状态")
    private Integer status;

    @ApiModelProperty("优惠总额")
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
		return "OrderReturnReasonVO{" +
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
