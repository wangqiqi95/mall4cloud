package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 分销收入记录信息DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public class DistributionUserIncomeDTO{
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

	@ApiModelProperty("分销业绩-推广效果：0无 1佣金 2创建时间 3更新时间")
	private Integer sortParam;

	@ApiModelProperty("排序类型 0无 1 正序 2倒序")
	private Integer sortType;

	@ApiModelProperty("商品名称")
	private String spuName;

	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("分销员")
	private DistributionUserDTO distributionUser;

	@ApiModelProperty("分销员昵称")
	private String nickName;

	@ApiModelProperty("分销员手机号")
	private String userMobile;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("店铺id列表")
	private List<Long> shopIds;

	@ApiModelProperty("商品id")
	private Long spuId;

	@ApiModelProperty("商品id列表")
	private List<Long> spuIds;

	public List<Long> getShopIds() {
		return shopIds;
	}

	public void setShopIds(List<Long> shopIds) {
		this.shopIds = shopIds;
	}

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

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

	public DistributionUserDTO getDistributionUser() {
		return distributionUser;
	}

	public void setDistributionUser(DistributionUserDTO distributionUser) {
		this.distributionUser = distributionUser;
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

	public Integer getSortParam() {
		if (sortParam == null) {
			return 0;
		}
		return sortParam;
	}

	public void setSortParam(Integer sortParam) {
		this.sortParam = sortParam;
	}

	public Integer getSortType() {
		if (sortType == null) {
			return 0;
		}
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		return "DistributionUserIncomeDTO{" +
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
				", sortParam=" + sortParam +
				", sortType=" + sortType +
				", spuName='" + spuName + '\'' +
				", shopName='" + shopName + '\'' +
				", distributionUser=" + distributionUser +
				", nickName='" + nickName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", shopId=" + shopId +
				", shopIds=" + shopIds +
				", spuId=" + spuId +
				", spuIds=" + spuIds +
				'}';
	}
}
