package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public class DistributionUserIncome extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 收入记录id
     */
    private Long incomeId;

    /**
     * 钱包id
     */
    private Long walletId;

    /**
	 * DistributionUserIncomeTypeEnum
     * 收入类型(1一代奖励、2二代奖励 3邀请奖励 等 )
     */
    private Integer incomeType;

    /**
	 * DistributionUserIncomeStateEnum
     * 佣金状态(0:待支付、1:待结算、2:已结算、-1:订单失效)
     */
    private Integer state;

    /**
     * 佣金数额
     */
    private Long incomeAmount;

    /**
     * 关联订购流水号
     */
    private Long orderId;

    /**
     * 关联订单项order_item_id
     */
    private Long orderItemId;

    /**
     * 商户订单号
     */
    private String merchantOrderId;

    /**
     * 分销员id
     */
    private Long distributionUserId;

    /**
     * 操作人id
     */
    private Long modifier;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 商品id
	 */
	private Long spuId;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Integer getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(Integer incomeType) {
		this.incomeType = incomeType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(Long incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return "DistributionUserIncome{" +
				"incomeId=" + incomeId +
				", walletId=" + walletId +
				", incomeType=" + incomeType +
				", state=" + state +
				", incomeAmount=" + incomeAmount +
				", orderId=" + orderId +
				", orderItemId=" + orderItemId +
				", merchantOrderId='" + merchantOrderId + '\'' +
				", distributionUserId=" + distributionUserId +
				", modifier=" + modifier +
				", shopId=" + shopId +
				", spuId=" + spuId +
				'}';
	}
}
