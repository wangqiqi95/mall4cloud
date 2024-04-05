package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 订单快递信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("发货方式 1快递 3无需物流")
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

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
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

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	@Override
	public String toString() {
		return "DeliveryOrderVO{" +
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
				", deliveryType=" + deliveryType +
				'}';
	}
}
