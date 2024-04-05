package com.mall4j.cloud.order.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 自提订单自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderSelfStation extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 自提点id
     */
    private Long stationId;

    /**
     * 自提人的手机
     */
    private String stationUserMobile;

    /**
     * 自提人的名字
     */
    private String stationUserName;

    /**
     * 自提时间(用户下单时选择)
     */
    private String stationTime;

    /**
     * 自提提货码
     */
    private String stationCode;

    /**
     * 上门自提点的地址
     */
    private String stationAddress;

    /**
     * 上门自提点的联系电话
     */
    private String stationPhone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationUserMobile() {
		return stationUserMobile;
	}

	public void setStationUserMobile(String stationUserMobile) {
		this.stationUserMobile = stationUserMobile;
	}

	public String getStationUserName() {
		return stationUserName;
	}

	public void setStationUserName(String stationUserName) {
		this.stationUserName = stationUserName;
	}

	public String getStationTime() {
		return stationTime;
	}

	public void setStationTime(String stationTime) {
		this.stationTime = stationTime;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(String stationAddress) {
		this.stationAddress = stationAddress;
	}

	public String getStationPhone() {
		return stationPhone;
	}

	public void setStationPhone(String stationPhone) {
		this.stationPhone = stationPhone;
	}

	@Override
	public String toString() {
		return "OrderSelfStation{" +
				"id=" + id +
				",updateTime=" + updateTime +
				",createTime=" + createTime +
				",shopId=" + shopId +
				",orderId=" + orderId +
				",stationId=" + stationId +
				",stationUserMobile=" + stationUserMobile +
				",stationUserName=" + stationUserName +
				",stationTime=" + stationTime +
				",stationCode=" + stationCode +
				",stationAddress=" + stationAddress +
				",stationPhone=" + stationPhone +
				'}';
	}
}
