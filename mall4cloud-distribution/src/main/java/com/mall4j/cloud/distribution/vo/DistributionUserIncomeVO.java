package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销收入记录信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public class DistributionUserIncomeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收入记录id")
    private Long incomeId;

    @ApiModelProperty("钱包id")
    private Long walletId;

    @ApiModelProperty("收入类型(1一代奖励、2二代奖励 3邀请奖励 等 )")
    private Integer incomeType;

    @ApiModelProperty("佣金状态(0:待支付、1:待结算、2:已结算、-1:订单失效)")
    private Integer state;

    @ApiModelProperty("佣金数额")
    private Long incomeAmount;

    @ApiModelProperty("关联订购流水号")
    private Long orderId;

    @ApiModelProperty("关联订单项order_item_id")
    private Long orderItemId;

    @ApiModelProperty("商户订单号")
    private String merchantOrderId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("操作人id")
    private Long modifier;

	@ApiModelProperty("分销员")
    private DistributionUserVO distributionUser;

	@ApiModelProperty("订单状态")
	private Integer orderStatus;

	@ApiModelProperty("商品id")
	private Long spuId;

	@ApiModelProperty("商品名称")
	private String spuName;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("商品图片")
	private String mainImgUrl;

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
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

	public DistributionUserVO getDistributionUser() {
		return distributionUser;
	}

	public void setDistributionUser(DistributionUserVO distributionUser) {
		this.distributionUser = distributionUser;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "DistributionUserIncomeVO{" +
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
				", distributionUser=" + distributionUser +
				", orderStatus=" + orderStatus +
				", spuId=" + spuId +
				", spuName='" + spuName + '\'' +
				", shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", spuImg='" + mainImgUrl + '\'' +
				'}';
	}
}
