package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
@Data
public class OrderItemVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单项ID")
    private Long orderItemId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("订单id")
    private Long orderId;

	@ApiModelProperty("订单编号")
	private String orderNumber;

    @ApiModelProperty("产品ID")
    private Long spuId;

    @ApiModelProperty("产品SkuID")
    private Long skuId;

    @ApiModelProperty("用户Id")
	private Long userId;

	@ApiModelProperty("最终的退款id")
	private Long finalRefundId;
	@ApiModelProperty("最终的退款编号")
	private String finalRefundNumber;

	@ApiModelProperty("推广员id")
	private Long distributionUserId;

	@ApiModelProperty("推广员类型")
	private Integer distributionUserType;

	@ApiModelProperty("购物车产品个数")
	private Integer count;

	@ApiModelProperty("产品名称")
	private String spuName;

	@ApiModelProperty("sku名称")
	private String skuName;

    @ApiModelProperty("产品主图片路径")
    private String pic;

    @ApiModelProperty("是否以评价(0.未评价1.已评价)")
    private Integer isComm;

    @ApiModelProperty("订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

    @ApiModelProperty("0全部发货 其他数量为剩余待发货数量")
    private Integer beDeliveredNum;

    @ApiModelProperty("单个orderItem的配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer deliveryType;

    @ApiModelProperty("加入购物车时间")
    private Date shopCartTime;

    @ApiModelProperty("产品价格")
    private Long price;

    @ApiModelProperty("商品总金额")
    private Long spuTotalAmount;

    @ApiModelProperty("商品实际金额 = 商品总金额 - 分摊的优惠金额")
    private Long actualTotal;

	@ApiModelProperty("分摊的优惠金额")
	private Long shareReduce;

	@ApiModelProperty("平台优惠金额")
	private Long platformShareReduce;

	@ApiModelProperty("推广员佣金")
	private Long distributionAmount;

	@ApiModelProperty("推广员佣金快照")
	private Long distributionAmountSnapshot;

	@ApiModelProperty("推广员佣金比例")
	private BigDecimal distributionAmountRate;

	@ApiModelProperty("上级推广员佣金")
	private Long distributionParentAmount;

	@ApiModelProperty("上级推广员佣金快照")
	private Long distributionParentAmountSnapshot;

	@ApiModelProperty("上级推广员佣金比例")
	private BigDecimal distributionParentAmountRate;

	@ApiModelProperty("积分价格（单价）")
	private Long scoreFee;

	@ApiModelProperty("使用积分")
	private Long useScore;

	@ApiModelProperty("获得积分")
	private Long gainScore;

	@ApiModelProperty(value = "评论时间", required = true)
	private Date commTime;

	@ApiModelProperty("发货改变的数量")
	private Integer changeNum;

	@ApiModelProperty("平台佣金")
	private Long platformCommission;

	@ApiModelProperty("佣金比例")
	private Double rate;

    @ApiModelProperty("店铺改价优惠金额")
    private Long shopChangeFreeAmount;

    @ApiModelProperty("订单项信息")
    private List<OrderItemLangVO> orderItemLangList;

    @ApiModelProperty("0-商品,1-赠品")
    private Integer type;

    @ApiModelProperty("分销退款结算状态 0-正常 1-退款")
    private Integer distributionRefundStatus;

    @ApiModelProperty("赠品活动ID")
    private Long giftActivityId;

    @ApiModelProperty("商品条码")
    private String skuCode;

    public String getFinalRefundNumber() {
        return finalRefundNumber;
	}

	public void setFinalRefundNumber(String finalRefundNumber) {
		this.finalRefundNumber = finalRefundNumber;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFinalRefundId() {
		return finalRefundId;
	}

	public void setFinalRefundId(Long finalRefundId) {
		this.finalRefundId = finalRefundId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getIsComm() {
		return isComm;
	}

	public void setIsComm(Integer isComm) {
		this.isComm = isComm;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Integer getBeDeliveredNum() {
		return beDeliveredNum;
	}

	public void setBeDeliveredNum(Integer beDeliveredNum) {
		this.beDeliveredNum = beDeliveredNum;
	}

	public Date getShopCartTime() {
		return shopCartTime;
	}

	public void setShopCartTime(Date shopCartTime) {
		this.shopCartTime = shopCartTime;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public Long getShareReduce() {
		return shareReduce;
	}

	public void setShareReduce(Long shareReduce) {
		this.shareReduce = shareReduce;
	}

	public Long getPlatformShareReduce() {
		return platformShareReduce;
	}

	public void setPlatformShareReduce(Long platformShareReduce) {
		this.platformShareReduce = platformShareReduce;
	}

	public Long getDistributionAmount() {
		return distributionAmount;
	}

	public void setDistributionAmount(Long distributionAmount) {
		this.distributionAmount = distributionAmount;
	}

	public Long getDistributionParentAmount() {
		return distributionParentAmount;
	}

	public void setDistributionParentAmount(Long distributionParentAmount) {
		this.distributionParentAmount = distributionParentAmount;
	}

	public Long getUseScore() {
		return useScore;
	}

	public void setUseScore(Long useScore) {
		this.useScore = useScore;
	}

	public Long getGainScore() {
		return gainScore;
	}

	public void setGainScore(Long gainScore) {
		this.gainScore = gainScore;
	}

	public Long getSpuTotalAmount() {
		return spuTotalAmount;
	}

	public void setSpuTotalAmount(Long spuTotalAmount) {
		this.spuTotalAmount = spuTotalAmount;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Date getCommTime() {
		return commTime;
	}

	public void setCommTime(Date commTime) {
		this.commTime = commTime;
	}

	public List<OrderItemLangVO> getOrderItemLangList() {
		return orderItemLangList;
	}

	public void setOrderItemLangList(List<OrderItemLangVO> orderItemLangList) {
		this.orderItemLangList = orderItemLangList;
	}

	public Long getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(Long platformCommission) {
		this.platformCommission = platformCommission;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Long getShopChangeFreeAmount() {
		return shopChangeFreeAmount;
	}

	public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
		this.shopChangeFreeAmount = shopChangeFreeAmount;
	}

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "OrderItemVO{" +
				"orderItemId=" + orderItemId +
				", shopId=" + shopId +
				", orderId=" + orderId +
				", spuId=" + spuId +
				", skuId=" + skuId +
				", userId=" + userId +
				", finalRefundId=" + finalRefundId +
				", distributionUserId=" + distributionUserId +
				", count=" + count +
				", spuName='" + spuName + '\'' +
				", skuName='" + skuName + '\'' +
				", pic='" + pic + '\'' +
				", isComm=" + isComm +
				", refundStatus=" + refundStatus +
				", beDeliveredNum=" + beDeliveredNum +
				", deliveryType=" + deliveryType +
				", shopCartTime=" + shopCartTime +
				", price=" + price +
				", spuTotalAmount=" + spuTotalAmount +
				", actualTotal=" + actualTotal +
				", shareReduce=" + shareReduce +
				", platformShareReduce=" + platformShareReduce +
				", distributionAmount=" + distributionAmount +
				", distributionParentAmount=" + distributionParentAmount +
				", scoreFee=" + scoreFee +
				", useScore=" + useScore +
				", gainScore=" + gainScore +
				", commTime=" + commTime +
				", changeNum=" + changeNum +
				", platformCommission=" + platformCommission +
				", shopChangeFreeAmount=" + shopChangeFreeAmount +
				", orderItemLangList=" + orderItemLangList +
				"} " + super.toString();
	}
}
