package com.mall4j.cloud.api.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 订单快递信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderFeignVO extends BaseVO{

    @ApiModelProperty("订单物流包裹id")
    private Long deliveryOrderId;

    @ApiModelProperty("订单号")
    private Long orderId;

    @ApiModelProperty("快递公司id")
    private Long deliveryCompanyId;

    @ApiModelProperty("快递单号")
    private String deliveryNo;

    @ApiModelProperty("收件人姓名")
    private String consigneeName;

    @ApiModelProperty("收件人电话（顺丰快递需要）")
    private String consigneeMobile;

    @ApiModelProperty("物流状态 1正常 -1删除")
    private Integer status;

    @ApiModelProperty("包裹商品总数")
    private Integer allCount;

    @ApiModelProperty("删除时间")
    private Date deleteTime;

	@ApiModelProperty("物流名称")
	private String deliveryName;

	@ApiModelProperty("物流包裹订单项")
	private List<DeliveryOrderItemFeignVO> orderItems;

	@ApiModelProperty("物流包裹订单项")
	private DeliveryInfoVO delivery;

	@ApiModelProperty("发货方式")
	private Integer deliveryType;

	public Long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(Long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public List<DeliveryOrderItemFeignVO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<DeliveryOrderItemFeignVO> orderItems) {
		this.orderItems = orderItems;
	}

	public DeliveryInfoVO getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryInfoVO delivery) {
		this.delivery = delivery;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	@Override
	public String toString() {
		return "DeliveryOrderFeignVO{" +
				"deliveryOrderId=" + deliveryOrderId +
				", orderId=" + orderId +
				", deliveryCompanyId=" + deliveryCompanyId +
				", deliveryNo='" + deliveryNo + '\'' +
				", consigneeName='" + consigneeName + '\'' +
				", consigneeMobile='" + consigneeMobile + '\'' +
				", status=" + status +
				", allCount=" + allCount +
				", deleteTime=" + deleteTime +
				", deliveryName='" + deliveryName + '\'' +
				", orderItems=" + orderItems +
				", delivery=" + delivery +
				", deliveryType=" + deliveryType +
				'}';
	}
}
