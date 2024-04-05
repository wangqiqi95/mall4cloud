package com.mall4j.cloud.order.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Data
public class Order extends BaseModel implements Serializable{
	private static final long serialVersionUID = 1L;


	/**
	 * 订单ID
	 */
	private Long orderId;

	/**
	 * 订单编号
	 */
	private String orderNumber;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 下单代购ID
	 */
	private Long buyStaffId;

	/**
	 * 用户订单地址Id
	 */
	private Long orderAddrId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 总值
	 */
	private Long total;

	/**
	 * 实际总值
	 */
	private Long actualTotal;

	/**
	 * 订单使用积分
	 */
	private Long orderScore;

	/**
	 * 订单发票id
	 */
	private Long orderInvoiceId;

	/**
	 * 买家备注
	 */
	private String remarks;

	/**
	 * 卖家备注
	 */
	private String shopRemarks;

	/**
	 * 订单状态 1:待付款 2:待发货(待自提) 3:待收货(已发货) 5:成功 6:失败 7:待成团
	 */
	private Integer status;

	/**
	 * 支付方式 请参考枚举PayType
	 */
	private Integer payType;

	/**
	 * 配送类型 1:快递 2:自提 3：无需快递 4同城配送
	 */
	private Integer deliveryType;

	/**
	 * 订单类型1团购订单 2秒杀订单 3积分订单
	 */
	private Integer orderType;

	/**
	 * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
	 */
	private Integer closeType;

	/**
	 * 订单商品总数
	 */
	private Integer allCount;

	/**
	 * 付款时间
	 */
	private Date payTime;

	/**
	 * 发货时间
	 */
	private Date deliveryTime;

	/**
	 * 完成时间
	 */
	private Date finallyTime;

	/**
	 * 结算时间
	 */
	private Date settledTime;

	/**
	 * 取消时间
	 */
	private Date cancelTime;

	/**
	 * 预售发货时间
	 */
	private Date bookTime;

	/**
	 * 是否已支付，1.已支付0.未支付
	 */
	private Integer isPayed;

	/**
	 * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
	 */
	private Integer deleteStatus;

	/**
	 * 订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
	 */
	private Integer refundStatus;

	/**
	 * 订单运费
	 */
	private Long freightAmount;

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
	 * 触点号
	 */
	private String tentacleNo;

	/**
	 * 分销关系 1分享关系 2服务关系 3自主下单 4代客下单
	 */
	private Integer distributionRelation;

	/**
	 * 分销佣金
	 */
	private Long distributionAmount;

	/**
	 * 分销佣金状态 0待结算 1已结算 2已提现 3提现中
	 */
	private Integer distributionStatus;

	/**
	 * 分销用户ID
	 */
	private Long distributionUserId;

	/**
	 * 分销用户类型 1-导购 2-微客
	 */
	private Integer distributionUserType;

	/**
	 * 分销用户门店ID
	 */
	private Long distributionStoreId;

	/**
	 * 分销佣金结算时间
	 */
	private Date distributionSettleTime;

	/**
	 * 分销佣金提现时间
	 */
	private Date distributionWithdrawTime;

	/**
	 * 发展佣金
	 */
	private Long developingAmount;

	/**
	 * 发展佣金状态 0待结算 1已结算 2已提现 3提现中
	 */
	private Integer developingStatus;

	/**
	 * 发展用户ID
	 */
	private Long developingUserId;

	/**
	 * 发展用户门店ID
	 */
	private Long developingStoreId;

	/**
	 * 发展佣金结算时间
	 */
	private Date developingSettleTime;

	/**
	 * 发展佣金提现时间
	 */
	private Date developingWithdrawTime;

	/**
	 * 平台优惠金额
	 */
	private Long platformAmount;

	/**
	 * 运费减免金额
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
	 * 优惠总额
	 */
	private Long reduceAmount;

	/**
	 * 平台佣金
	 */
	private Long platformCommission;

	/**
	 * 是否已经进行结算
	 */
	private Long isSettled;

	/**
	 * 订单项
	 */
	private List<OrderItem> orderItems;

	/**
	 * 订单地址
	 */
	private OrderAddr orderAddr;

	/**
	 * 收货人姓名
	 */
	private String consignee;

	/**
	 * 收货人手机号
	 */
	private String mobile;

	@ApiModelProperty(value = "门店id")
	private Long storeId;

    /**
     * 平台备注
     */
	private String platformRemarks;

	@ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单")
	private Integer orderSource;
	@ApiModelProperty(value = "来源id")
	private String sourceId;

	@ApiModelProperty(value = "视频号,跟踪ID，有效期十分钟，会影响主播归因、分享员归因等，需创建订单前调用，调用生成订单 api 时需传入该参数")
	private String traceId;
	@ApiModelProperty(value = "微信侧订单id")
	private Long wechatOrderId;

	/**
	 * 联营分佣结算状态 0正常 1分佣中 2分佣完成
	 */
	private Integer jointVentureCommissionStatus;

	/**
	 * 联营分佣结算后退款状态 0正常 1有退款
	 */
	private Integer jointVentureRefundStatus;

    public String getPlatformRemarks() {
        return platformRemarks;
    }

    public void setPlatformRemarks(String platformRemarks) {
        this.platformRemarks = platformRemarks;
    }

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderAddrId() {
		return orderAddrId;
	}

	public void setOrderAddrId(Long orderAddrId) {
		this.orderAddrId = orderAddrId;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public String getRemarks() {
		return remarks;
	}

	public Long getOrderInvoiceId() {
		return orderInvoiceId;
	}

	public void setOrderInvoiceId(Long orderInvoiceId) {
		this.orderInvoiceId = orderInvoiceId;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getShopRemarks() {
		return shopRemarks;
	}

	public void setShopRemarks(String shopRemarks) {
		this.shopRemarks = shopRemarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getCloseType() {
		return closeType;
	}

	public void setCloseType(Integer closeType) {
		this.closeType = closeType;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getFinallyTime() {
		return finallyTime;
	}

	public void setFinallyTime(Date finallyTime) {
		this.finallyTime = finallyTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Long getFreightAmount() {
		return freightAmount;
	}

	public void setFreightAmount(Long freightAmount) {
		this.freightAmount = freightAmount;
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

	public Long getPlatformAmount() {
		return platformAmount;
	}

	public void setPlatformAmount(Long platformAmount) {
		this.platformAmount = platformAmount;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getFreeFreightAmount() {
		return freeFreightAmount;
	}

	public void setFreeFreightAmount(Long freeFreightAmount) {
		this.freeFreightAmount = freeFreightAmount;
	}

	public Long getShopChangeFreeAmount() {
		return shopChangeFreeAmount;
	}

	public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
		this.shopChangeFreeAmount = shopChangeFreeAmount;
	}

	public Long getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(Long platformCommission) {
		this.platformCommission = platformCommission;
	}

	public Long getPlatformFreeFreightAmount() {
		return platformFreeFreightAmount;
	}

	public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
		this.platformFreeFreightAmount = platformFreeFreightAmount;
	}

	public Long getOrderScore() {
		return orderScore;
	}

	public void setOrderScore(Long orderScore) {
		this.orderScore = orderScore;
	}

	public OrderAddr getOrderAddr() {
		return orderAddr;
	}

	public void setOrderAddr(OrderAddr orderAddr) {
		this.orderAddr = orderAddr;
	}

	public Long getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(Long isSettled) {
		this.isSettled = isSettled;
	}

	public String getTentacleNo() {
		return tentacleNo;
	}

	public void setTentacleNo(String tentacleNo) {
		this.tentacleNo = tentacleNo;
	}

	public Integer getDistributionRelation() {
		return distributionRelation;
	}

	public void setDistributionRelation(Integer distributionRelation) {
		this.distributionRelation = distributionRelation;
	}

	public Long getDistributionAmount() {
		return distributionAmount;
	}

	public void setDistributionAmount(Long distributionAmount) {
		this.distributionAmount = distributionAmount;
	}

	public Integer getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(Integer distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Integer getDistributionUserType() {
		return distributionUserType;
	}

	public void setDistributionUserType(Integer distributionUserType) {
		this.distributionUserType = distributionUserType;
	}

	public Long getDevelopingAmount() {
		return developingAmount;
	}

	public void setDevelopingAmount(Long developingAmount) {
		this.developingAmount = developingAmount;
	}

	public Integer getDevelopingStatus() {
		return developingStatus;
	}

	public void setDevelopingStatus(Integer developingStatus) {
		this.developingStatus = developingStatus;
	}

	public Long getDevelopingUserId() {
		return developingUserId;
	}

	public void setDevelopingUserId(Long developingUserId) {
		this.developingUserId = developingUserId;
	}

	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getDistributionStoreId() {
		return distributionStoreId;
	}

	public void setDistributionStoreId(Long distributionStoreId) {
		this.distributionStoreId = distributionStoreId;
	}

	public Long getDevelopingStoreId() {
		return developingStoreId;
	}

	public void setDevelopingStoreId(Long developingStoreId) {
		this.developingStoreId = developingStoreId;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderId=" + orderId +
				", shopId=" + shopId +
				", userId=" + userId +
				", orderAddrId=" + orderAddrId +
				", shopName='" + shopName + '\'' +
				", total=" + total +
				", actualTotal=" + actualTotal +
				", orderScore=" + orderScore +
				", orderInvoiceId=" + orderInvoiceId +
				", remarks='" + remarks + '\'' +
				", shopRemarks='" + shopRemarks + '\'' +
				", status=" + status +
				", payType=" + payType +
				", deliveryType=" + deliveryType +
				", orderType=" + orderType +
				", closeType=" + closeType +
				", allCount=" + allCount +
				", payTime=" + payTime +
				", deliveryTime=" + deliveryTime +
				", finallyTime=" + finallyTime +
				", settledTime=" + settledTime +
				", cancelTime=" + cancelTime +
				", bookTime=" + bookTime +
				", isPayed=" + isPayed +
				", deleteStatus=" + deleteStatus +
				", refundStatus=" + refundStatus +
				", freightAmount=" + freightAmount +
				", scoreAmount=" + scoreAmount +
				", memberAmount=" + memberAmount +
				", platformCouponAmount=" + platformCouponAmount +
				", shopCouponAmount=" + shopCouponAmount +
				", discountAmount=" + discountAmount +
				", tentacleNo='" + tentacleNo + '\'' +
				", distributionRelation=" + distributionRelation +
				", distributionAmount=" + distributionAmount +
				", distributionStatus=" + distributionStatus +
				", distributionUserId=" + distributionUserId +
				", distributionUserType=" + distributionUserType +
				", distributionStoreId=" + distributionStoreId +
				", developingAmount=" + developingAmount +
				", developingStatus=" + developingStatus +
				", developingUserId=" + developingUserId +
				", developingStoreId=" + developingStoreId +
				", platformAmount=" + platformAmount +
				", freeFreightAmount=" + freeFreightAmount +
				", platformFreeFreightAmount=" + platformFreeFreightAmount +
				", shopChangeFreeAmount=" + shopChangeFreeAmount +
				", reduceAmount=" + reduceAmount +
				", platformCommission=" + platformCommission +
				", isSettled=" + isSettled +
				", orderItems=" + orderItems +
				", orderAddr=" + orderAddr +
				", consignee='" + consignee + '\'' +
				", mobile='" + mobile + '\'' +
				'}';
	}
}
