package com.mall4j.cloud.order.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单项
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
@Data
public class OrderItem extends BaseModel implements Serializable{

    /**
     * 订单项ID
     */
    private Long orderItemId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单id
     */
    private Long orderId;

	/**
	 * 订单编号
	 */
	private String orderNumber;

	/**
	 * 分类id
	 */
	private Long categoryId;

    /**
     * 产品ID
     */
    private Long spuId;

    /**
     * 产品SkuID
     */
    private Long skuId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 最终的退款id
     */
    private Long finalRefundId;

    /**
     * 推广员id
     */
    private Long distributionUserId;

    /**
     * 购物车产品个数
     */
    private Integer count;

    /**
     * 产品名称
     */
    private String spuName;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 产品主图片路径
     */
    private String pic;

    /**
     * 是否以评价(0.未评价1.已评价)
     */
    private Integer isComm;

	/**
	 * 评论时间
	 */
	private Date commTime;

    /**
     * 订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
     */
    private Integer refundStatus;

    /**
     * -1待发货 0全部发货 其他数量为剩余待发货数量
     */
    private Integer beDeliveredNum;

    /**
     * 单个orderItem的配送类型 1:快递 2:自提 3：无需快递 4:同城配送
     */
    private Integer deliveryType;

    /**
     * 加入购物车时间
     */
    private Date shopCartTime;

    /**
     * 产品价格
     */
    private Long price;

    /**
     * 商品总金额
     */
    private Long spuTotalAmount;

    /**
     * 商品实际金额 = 商品总金额 - 分摊的优惠金额
     */
    private Long actualTotal;

    /**
     * 分摊的优惠金额
     */
    private Long shareReduce;

	/**
	 * 平台优惠金额
	 */
	private Long platformShareReduce;

	/**
	 * 推广员佣金
	 */
	private Long distributionAmount;

	/**
	 * 推广员佣金
	 */
	private Long distributionAmountSnapshot;

	/**
	 * 上级推广员佣金
	 */
	private Long distributionParentAmount;

	/**
	 * 上级推广员佣金快照
	 */
	private Long distributionParentAmountSnapshot;

	/**
	 * 积分价格（单价）
	 */
	private Long scoreFee;

	/**
	 * 使用积分
	 */
	private Long useScore;

	/**
     * 获得积分
     */
    private Long gainScore;

	/**
	 * 分账比例
	 */
	private Double rate;

	/**
	 * 平台佣金(商品实际金额 * 分账比例)
	 */
	private Long platformCommission;

	/**
	 * 积分抵扣金额
	 */
	private Long scoreAmount;

	/**
	 * 会员折扣金额
	 */
	private Long memberAmount;

	/**
	 * 平台优惠券优惠金额
	 */
	private Long platformCouponAmount;

	/**
	 * 商家优惠券优惠金额
	 */
	private Long shopCouponAmount;

	/**
	 * 满减优惠金额
	 */
	private Long discountAmount;


	/**
	 * 商家运费减免金额
	 */
	private Long freeFreightAmount;

	/**
	 * 平台运费减免金额
	 */
	private Long platformFreeFreightAmount;

	/**
	 * 店铺改价优惠金额
	 */
	private Long shopChangeFreeAmount;


	/**
	 * 订单项国际化信息
	 */
	private List<OrderItemLang> orderItemLangList;

	private Integer type;

	private Long giftActivityId;

	private String skuCode;

	/**
	 * 联营分佣结算状态 0未结算 1结算中 2结算完成
	 */
	private Integer jointVentureCommissionStatus;

	/**
	 * 联营分佣结算退款状态 0正常 1退款
	 */
	private Integer jointVentureRefundStatus;

	/**
     * 分销退款结算状态  0-正常 1-退款
     */
	private Integer distributionRefundStatus;


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

	public Long getSpuTotalAmount() {
		return spuTotalAmount;
	}

	public void setSpuTotalAmount(Long spuTotalAmount) {
		this.spuTotalAmount = spuTotalAmount;
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

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
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

	public List<OrderItemLang> getOrderItemLangList() {
		return orderItemLangList;
	}

	public void setOrderItemLangList(List<OrderItemLang> orderItemLangList) {
		this.orderItemLangList = orderItemLangList;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Long getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(Long platformCommission) {
		this.platformCommission = platformCommission;
	}

	public Long getScoreAmount() {
		return scoreAmount;
	}

	public void setScoreAmount(Long scoreAmount) {
		this.scoreAmount = scoreAmount;
	}

	public Long getMemberAmount() {
		return memberAmount;
	}

	public void setMemberAmount(Long memberAmount) {
		this.memberAmount = memberAmount;
	}

	public Long getPlatformCouponAmount() {
		return platformCouponAmount;
	}

	public void setPlatformCouponAmount(Long platformCouponAmount) {
		this.platformCouponAmount = platformCouponAmount;
	}

	public Long getShopCouponAmount() {
		return shopCouponAmount;
	}

	public void setShopCouponAmount(Long shopCouponAmount) {
		this.shopCouponAmount = shopCouponAmount;
	}

	public Long getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Long discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Long getPlatformFreeFreightAmount() {
		return platformFreeFreightAmount;
	}

	public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
		this.platformFreeFreightAmount = platformFreeFreightAmount;
	}

	public Long getShopChangeFreeAmount() {
		return shopChangeFreeAmount;
	}

	public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
		this.shopChangeFreeAmount = shopChangeFreeAmount;
	}

	public Long getFreeFreightAmount() {
		return freeFreightAmount;
	}

	public void setFreeFreightAmount(Long freeFreightAmount) {
		this.freeFreightAmount = freeFreightAmount;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "OrderItem{" +
				"orderItemId=" + orderItemId +
				", shopId=" + shopId +
				", orderId=" + orderId +
				", categoryId=" + categoryId +
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
				", commTime=" + commTime +
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
				", rate=" + rate +
				", platformCommission=" + platformCommission +
				", scoreAmount=" + scoreAmount +
				", memberAmount=" + memberAmount +
				", platformCouponAmount=" + platformCouponAmount +
				", shopCouponAmount=" + shopCouponAmount +
				", discountAmount=" + discountAmount +
				", freeFreightAmount=" + freeFreightAmount +
				", platformFreeFreightAmount=" + platformFreeFreightAmount +
				", shopChangeFreeAmount=" + shopChangeFreeAmount +
				", orderItemLangList=" + orderItemLangList +
				"} " + super.toString();
	}
}
