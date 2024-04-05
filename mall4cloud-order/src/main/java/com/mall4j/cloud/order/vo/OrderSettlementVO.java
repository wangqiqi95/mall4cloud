package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 订单结算表VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderSettlementVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付结算单据ID")
    private Long settlementId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("支付单号")
    private Long payId;

    @ApiModelProperty("是否清算 0:否 1:是")
    private Integer isClearing;

    @ApiModelProperty("支付积分")
    private Long payScore;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("清算时间")
    private Date clearingTime;

    @ApiModelProperty("版本号")
    private Integer version;

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getIsClearing() {
		return isClearing;
	}

	public void setIsClearing(Integer isClearing) {
		this.isClearing = isClearing;
	}

	public Long getPayScore() {
		return payScore;
	}

	public void setPayScore(Long payScore) {
		this.payScore = payScore;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public Date getClearingTime() {
		return clearingTime;
	}

	public void setClearingTime(Date clearingTime) {
		this.clearingTime = clearingTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "OrderSettlementVO{" +
				"settlementId=" + settlementId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",orderId=" + orderId +
				",payId=" + payId +
				",isClearing=" + isClearing +
				",payScore=" + payScore +
				",payAmount=" + payAmount +
				",clearingTime=" + clearingTime +
				",version=" + version +
				'}';
	}
}
