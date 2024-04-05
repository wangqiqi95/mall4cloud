package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class Transport extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 运费模板id
     */
    private Long transportId;

    /**
     * 运费模板名称
     */
    private String transName;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 收费方式（0 按件数,1 按重量 2 按体积）
     */
    private Integer chargeType;

    /**
     * 是否包邮 0:不包邮 1:包邮
     */
    private Integer isFreeFee;

    /**
     * 是否含有包邮条件 0 否 1是
     */
    private Integer hasFreeCondition;

	public Long getTransportId() {
		return transportId;
	}

	public void setTransportId(Long transportId) {
		this.transportId = transportId;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getIsFreeFee() {
		return isFreeFee;
	}

	public void setIsFreeFee(Integer isFreeFee) {
		this.isFreeFee = isFreeFee;
	}

	public Integer getHasFreeCondition() {
		return hasFreeCondition;
	}

	public void setHasFreeCondition(Integer hasFreeCondition) {
		this.hasFreeCondition = hasFreeCondition;
	}

	@Override
	public String toString() {
		return "Transport{" +
				"transportId=" + transportId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transName=" + transName +
				",shopId=" + shopId +
				",chargeType=" + chargeType +
				",isFreeFee=" + isFreeFee +
				",hasFreeCondition=" + hasFreeCondition +
				'}';
	}
}
