package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 我的订单
 *
 * @author FrozenWatermelon
 */
@ApiModel("我的订单")
public class MyOrderVO {

	@ApiModelProperty(value = "订单项",required=true)
	private List<MyOrderItemVO> orderItems;

	@ApiModelProperty(value = "订单号",required=true)
	private Long orderId;

	@ApiModelProperty(value = "总价",required=true)
	private Long actualTotal;

	@ApiModelProperty(value = "使用积分",required=true)
	private Long orderScore;

	@ApiModelProperty(value = "订单状态",required=true)
	private Integer status;

	@ApiModelProperty(value = "订单类型(0普通订单 1团购订单 2秒杀订单)",required=true)
	private Integer orderType;

	@ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）",required=true)
	private Integer refundType;

	@ApiModelProperty(value = "处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)",required=true)
	private Integer returnMoneySts;

	@ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递",required=true)
	private Integer deliveryType;

	@ApiModelProperty(value = "店铺名称",required=true)
	private String shopName;

	@ApiModelProperty(value = "店铺id",required=true)
	private Long shopId;

	@ApiModelProperty(value = "订单运费",required=true)
	private Long freightAmount;

	@ApiModelProperty(value = "订单创建时间",required=true)
	private Date createTime;

	@ApiModelProperty(value = "商品总数",required=true)
	private Integer allCount;

	@ApiModelProperty(value = "用户备注信息")
	private String remarks;

	public List<MyOrderItemVO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<MyOrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public Long getOrderScore() {
		return orderScore;
	}

	public void setOrderScore(Long orderScore) {
		this.orderScore = orderScore;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Integer getReturnMoneySts() {
		return returnMoneySts;
	}

	public void setReturnMoneySts(Integer returnMoneySts) {
		this.returnMoneySts = returnMoneySts;
	}


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getFreightAmount() {
		return freightAmount;
	}

	public void setFreightAmount(Long freightAmount) {
		this.freightAmount = freightAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	@Override
	public String toString() {
		return "MyOrderVO{" +
				"orderItems=" + orderItems +
				", orderId=" + orderId +
				", actualTotal=" + actualTotal +
				", orderScore=" + orderScore +
				", status=" + status +
				", orderType=" + orderType +
				", refundType=" + refundType +
				", returnMoneySts=" + returnMoneySts +
				", deliveryType=" + deliveryType +
				", shopName='" + shopName + '\'' +
				", shopId=" + shopId +
				", freightAmount=" + freightAmount +
				", createTime=" + createTime +
				", allCount=" + allCount +
				", remarks='" + remarks + '\'' +
				'}';
	}
}
