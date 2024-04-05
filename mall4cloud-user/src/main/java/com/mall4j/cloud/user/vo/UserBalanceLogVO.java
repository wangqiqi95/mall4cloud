package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 余额记录VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserBalanceLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long balanceLogId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("改变金额")
    private Long changeBalance;

    @ApiModelProperty("收支类型 0支出 1收入")
    private Integer ioType;

    @ApiModelProperty("支付编号(用于余额充值)")
    private Long payId;

    @ApiModelProperty("1:充值 2:赠送 3:支付  4:退款 5:平台手动修改")
    private Integer type;

    @ApiModelProperty("1:已支付 0:未支付(用于余额充值)")
    private Integer isPayed;

    @ApiModelProperty("退款单号")
	private Long refundId;

    @ApiModelProperty("订单号列表")
	private List<Long> orderIds;

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public Long getBalanceLogId() {
		return balanceLogId;
	}

	public void setBalanceLogId(Long balanceLogId) {
		this.balanceLogId = balanceLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getChangeBalance() {
		return changeBalance;
	}

	public void setChangeBalance(Long changeBalance) {
		this.changeBalance = changeBalance;
	}

	public Integer getIoType() {
		return ioType;
	}

	public void setIoType(Integer ioType) {
		this.ioType = ioType;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	@Override
	public String toString() {
		return "UserBalanceLogVO{" +
				"balanceLogId=" + balanceLogId +
				", userId=" + userId +
				", changeBalance=" + changeBalance +
				", ioType=" + ioType +
				", payId=" + payId +
				", type=" + type +
				", isPayed=" + isPayed +
				", refundId=" + refundId +
				", orderIds=" + orderIds +
				'}';
	}
}
