package com.mall4j.cloud.api.docking.skq_crm.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：订单查询返回响应
 *
 * @date 2022/1/26 12:32：16
 */
public class OrderSelectVo implements Serializable {

	private static final long serialVersionUID = -5058253272912462250L;
	@ApiModelProperty(value = "订单编号")
	private String orderId;

	@ApiModelProperty(value = "订单渠道JD/pos/ TAOBAO/web")
	private String channelType;

	@ApiModelProperty(value = "门店编码")
	private String shopCode;

	@ApiModelProperty(value = "门店名称")
	private String shopName;

	@ApiModelProperty(value = "实付金额")
	private BigDecimal payment;

	@ApiModelProperty(value = "付款时间,2022-11-08 23:59:59")
	private String payTime;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Override public String toString() {
		return "OrderSelectVo{" + "orderId='" + orderId + '\'' + ", channelType='" + channelType + '\'' + ", shopCode='" + shopCode + '\'' + ", shopName='"
				+ shopName + '\'' + ", payment=" + payment + ", payTime='" + payTime + '\'' + '}';
	}
}
