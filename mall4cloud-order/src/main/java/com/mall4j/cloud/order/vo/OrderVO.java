package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderVO extends BaseVO {

	@ApiModelProperty(value = "订单ID")
	private Long orderId;

	@ApiModelProperty("订单编号")
	private String orderNumber;

	@ApiModelProperty(value = "店铺id")
	private Long shopId;

	@ApiModelProperty(value = "用户ID")
	private Long userId;

	@ApiModelProperty("用户名称")
	private String userName;

	@ApiModelProperty("用户手机号")
	private String userMobile;

	@ApiModelProperty(value = "用户订单地址Id")
	private Long orderAddrId;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "总值")
	private Long total;

	@ApiModelProperty(value = "实际总值")
	private Long actualTotal;

	@ApiModelProperty(value = "订单使用积分")
	private Long orderScore;

	@ApiModelProperty(value = "买家备注")
	private String remarks;

    @ApiModelProperty(value = "平台备注")
    private String platformRemarks;

	@ApiModelProperty(value = "卖家备注")
	private String shopRemarks;

	@ApiModelProperty(value = "订单状态 1:待付款 2:待发货(待自提) 3:待收货(已发货) 5:成功 6:失败 7:待成团")
	private Integer status;

	@ApiModelProperty(value = "支付方式 请参考枚举PayType")
	private Integer payType;

	@ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4同城配送")
	private Integer deliveryType;

	@ApiModelProperty(value = "订单类型1团购订单 2秒杀订单 3积分订单")
	private Integer orderType;

	@ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单")
	private Integer orderSource;

	@ApiModelProperty(value = "订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易")
	private Integer closeType;

	@ApiModelProperty(value = "订单商品总数")
	private Integer allCount;

	@ApiModelProperty(value = "付款时间")
	private Date payTime;

	@ApiModelProperty(value = "发货时间")
	private Date deliveryTime;

	@ApiModelProperty(value = "完成时间")
	private Date finallyTime;

	@ApiModelProperty(value = "取消时间")
	private Date cancelTime;

	@ApiModelProperty(value = "预售发货时间")
	private Date bookTime;

	@ApiModelProperty(value = "是否已支付，1.已支付0.未支付")
	private Integer isPayed;

	@ApiModelProperty(value = "用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除")
	private Integer deleteStatus;

	@ApiModelProperty(value = "订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
	private Integer refundStatus;

	@ApiModelProperty(value = "订单运费")
	private Long freightAmount;

	@ApiModelProperty(value = "积分抵扣金额")
	private Long scoreAmount;

	@ApiModelProperty(value = "会员折扣金额")
	private Long memberAmount;

	@ApiModelProperty(value = "平台优惠券优惠金额")
	private Long platformCouponAmount;

	@ApiModelProperty(value = "商家优惠券优惠金额")
	private Long shopCouponAmount;

	@ApiModelProperty(value = "满减优惠金额")
	private Long discountAmount;

	@ApiModelProperty(value = "平台优惠金额")
	private Long platformAmount;

	@ApiModelProperty(value = "平台运费减免金额")
	private Long platformFreeFreightAmount;

	@ApiModelProperty(value = "优惠总额")
	private Long reduceAmount;

	@ApiModelProperty(value = "订单项")
	private List<OrderItemVO> orderItems;

	@ApiModelProperty(value = "订单地址")
	private OrderAddrVO orderAddr;

	@ApiModelProperty(value = "包裹信息")
	private List<DeliveryOrderFeignVO> deliveryExpresses;

	@ApiModelProperty(value = "物流Id")
	private Long dvyId;

	@ApiModelProperty(value = "物流单号")
	private String dvyFlowId;

	@ApiModelProperty(value = "商家运费减免金额")
	private Long freeFreightAmount;

	@ApiModelProperty(value = "订单使用优惠券明细")
	private List<TCouponUserOrderDetailVO> orderCouponDetailVO;

	@ApiModelProperty("最终的退款id")
	private Long finalRefundId;

	@ApiModelProperty(value = "当前可退款金额")
	private Long canRefundAmount;

	@ApiModelProperty(value = "能否退款")
	private Boolean canRefund;

	@ApiModelProperty(value = "能否整单退款")
	private Boolean canAllRefund;

	public Integer getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}

	public Boolean getCanRefund() {
		return canRefund;
	}

	public void setCanRefund(Boolean canRefund) {
		this.canRefund = canRefund;
	}

	public Boolean getCanAllRefund() {
		return canAllRefund;
	}

	public void setCanAllRefund(Boolean canAllRefund) {
		this.canAllRefund = canAllRefund;
	}

	public Long getFinalRefundId() {
		return finalRefundId;
	}

	public void setFinalRefundId(Long finalRefundId) {
		this.finalRefundId = finalRefundId;
	}

	public Long getCanRefundAmount() {
		return canRefundAmount;
	}

	public void setCanRefundAmount(Long canRefundAmount) {
		this.canRefundAmount = canRefundAmount;
	}

	public List<TCouponUserOrderDetailVO> getOrderCouponDetailVO() {
		return orderCouponDetailVO;
	}

	public void setOrderCouponDetailVO(List<TCouponUserOrderDetailVO> orderCouponDetailVO) {
		this.orderCouponDetailVO = orderCouponDetailVO;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getPlatformRemarks() {
        return platformRemarks;
    }

    public void setPlatformRemarks(String platformRemarks) {
        this.platformRemarks = platformRemarks;
    }

    public Long getFreeFreightAmount() {
		return freeFreightAmount;
	}

	public void setFreeFreightAmount(Long freeFreightAmount) {
		this.freeFreightAmount = freeFreightAmount;
	}

	public Long getPlatformFreeFreightAmount() {
		return platformFreeFreightAmount;
	}

	public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
		this.platformFreeFreightAmount = platformFreeFreightAmount;
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

	public List<OrderItemVO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getOrderScore() {
		return orderScore;
	}

	public void setOrderScore(Long orderScore) {
		this.orderScore = orderScore;
	}

	public OrderAddrVO getOrderAddr() {
		return orderAddr;
	}

	public void setOrderAddr(OrderAddrVO orderAddr) {
		this.orderAddr = orderAddr;
	}

	public Long getDvyId() {
		return dvyId;
	}

	public void setDvyId(Long dvyId) {
		this.dvyId = dvyId;
	}

	public String getDvyFlowId() {
		return dvyFlowId;
	}

	public void setDvyFlowId(String dvyFlowId) {
		this.dvyFlowId = dvyFlowId;
	}

	public List<DeliveryOrderFeignVO> getDeliveryExpresses() {
		return deliveryExpresses;
	}

	public void setDeliveryExpresses(List<DeliveryOrderFeignVO> deliveryExpresses) {
		this.deliveryExpresses = deliveryExpresses;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "OrderVO{" +
				"orderId=" + orderId +
				", shopId=" + shopId +
				", userId=" + userId +
				", orderAddrId=" + orderAddrId +
				", shopName='" + shopName + '\'' +
				", total=" + total +
				", actualTotal=" + actualTotal +
				", orderScore=" + orderScore +
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
				", platformAmount=" + platformAmount +
				", platformFreeFreightAmount=" + platformFreeFreightAmount +
				", reduceAmount=" + reduceAmount +
				", orderItems=" + orderItems +
				", orderAddr=" + orderAddr +
				", deliveryExpresses=" + deliveryExpresses +
				", dvyId=" + dvyId +
				", dvyFlowId='" + dvyFlowId + '\'' +
				", freeFreightAmount=" + freeFreightAmount +
				", platformFreeFreightAmount=" + platformFreeFreightAmount +
				'}';
	}
}
