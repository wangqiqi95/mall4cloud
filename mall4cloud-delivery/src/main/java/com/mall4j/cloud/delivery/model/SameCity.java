package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 同城配送信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-16 15:33:57
 */
public class SameCity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 同城配送
     */
    private Long samecityId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 配送区域经纬度json
     */
    private String positionInfo;

    /**
     * 收费类型 1按区域收取固定配送费 2按距离收取配送费
     */
    private Integer chargeType;

    /**
     * 起送价 是优惠券/码和满减优惠抵扣前的商品金额，运费不计入起送价。
     */
    private Long startFee;

    /**
     * 配送费
     */
    private Long deliveryFee;

    /**
     * 默认距离(km)
     */
    private Double defaultDistance;

    /**
     * 每超出距离(km)
     */
    private Double overDistance;

    /**
     * 每超出距离费用
     */
    private Long overDistanceFee;

    /**
     * 免费重量
     */
    private Double freeWeight;

    /**
     * 续重重量
     */
    private Double overWeight;

    /**
     * 续重费用
     */
    private Long overWeightFee;

    /**
     * 启用状态 1启用 0未启用
     */
    private Integer status;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

	public Long getSamecityId() {
		return samecityId;
	}

	public void setSamecityId(Long samecityId) {
		this.samecityId = samecityId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(String positionInfo) {
		this.positionInfo = positionInfo;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Long getStartFee() {
		return startFee;
	}

	public void setStartFee(Long startFee) {
		this.startFee = startFee;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Long deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Double getDefaultDistance() {
		return defaultDistance;
	}

	public void setDefaultDistance(Double defaultDistance) {
		this.defaultDistance = defaultDistance;
	}

	public Double getOverDistance() {
		return overDistance;
	}

	public void setOverDistance(Double overDistance) {
		this.overDistance = overDistance;
	}

	public Long getOverDistanceFee() {
		return overDistanceFee;
	}

	public void setOverDistanceFee(Long overDistanceFee) {
		this.overDistanceFee = overDistanceFee;
	}

	public Double getFreeWeight() {
		return freeWeight;
	}

	public void setFreeWeight(Double freeWeight) {
		this.freeWeight = freeWeight;
	}

	public Double getOverWeight() {
		return overWeight;
	}

	public void setOverWeight(Double overWeight) {
		this.overWeight = overWeight;
	}

	public Long getOverWeightFee() {
		return overWeightFee;
	}

	public void setOverWeightFee(Long overWeightFee) {
		this.overWeightFee = overWeightFee;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "SameCity{" +
				"samecityId=" + samecityId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",positionInfo=" + positionInfo +
				",chargeType=" + chargeType +
				",startFee=" + startFee +
				",deliveryFee=" + deliveryFee +
				",defaultDistance=" + defaultDistance +
				",overDistance=" + overDistance +
				",overDistanceFee=" + overDistanceFee +
				",freeWeight=" + freeWeight +
				",overWeight=" + overWeight +
				",overWeightFee=" + overWeightFee +
				",status=" + status +
				",lng=" + lng +
				",lat=" + lat +
				'}';
	}
}
