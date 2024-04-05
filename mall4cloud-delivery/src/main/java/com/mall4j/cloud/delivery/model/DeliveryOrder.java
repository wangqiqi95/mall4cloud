package com.mall4j.cloud.delivery.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单快递信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrder extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 订单物流包裹id
     */
    private Long deliveryOrderId;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 快递公司id
     */
    private Long deliveryCompanyId;

    /**
     * 快递单号
     */
    private String deliveryNo;

    /**
     * 收件人姓名
     */
    private String consigneeName;

    /**
     * 收件人电话（顺丰快递需要）
     */
    private String consigneeMobile;

    /**
     * 物流状态 1正常 -1删除
     */
    private Integer status;

    /**
     * 包裹商品总数
     */
    private Integer allCount;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 物流公司名称
     */
    private String companyName;

	/**
	 * 包裹项
	 */
	private List<DeliveryOrderItem> deliveryOrderItemList;

	/**
	 * 发货方式[1.快递 3.无需物流]
	 */
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

	public List<DeliveryOrderItem> getDeliveryOrderItemList() {
		return deliveryOrderItemList;
	}

	public void setDeliveryOrderItemList(List<DeliveryOrderItem> deliveryOrderItemList) {
		this.deliveryOrderItemList = deliveryOrderItemList;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	@Override
	public String toString() {
		return "DeliveryOrder{" +
				"deliveryOrderId=" + deliveryOrderId +
				", orderId=" + orderId +
				", deliveryCompanyId=" + deliveryCompanyId +
				", deliveryNo='" + deliveryNo + '\'' +
				", consigneeName='" + consigneeName + '\'' +
				", consigneeMobile='" + consigneeMobile + '\'' +
				", status=" + status +
				", allCount=" + allCount +
				", deleteTime=" + deleteTime +
				", companyName='" + companyName + '\'' +
				", deliveryOrderItemList=" + deliveryOrderItemList +
				", deliveryType=" + deliveryType +
				'}';
	}
}
