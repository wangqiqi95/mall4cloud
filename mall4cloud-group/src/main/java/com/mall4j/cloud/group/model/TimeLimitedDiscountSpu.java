package com.mall4j.cloud.group.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 限时调价活动 spu价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
public class TimeLimitedDiscountSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer activityId;

    /**
     * 
     */
    private Integer spuId;

    /**
     * 参与方式 0 按货号 1按条码 2按skuCode
     */
    private Integer participationMode;

    /**
     * 售价，整数方式保存
     */
    private Long price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getSpuId() {
		return spuId;
	}

	public void setSpuId(Integer spuId) {
		this.spuId = spuId;
	}

	public Integer getParticipationMode() {
		return participationMode;
	}

	public void setParticipationMode(Integer participationMode) {
		this.participationMode = participationMode;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "TimeLimitedDiscountSpu{" +
				"id=" + id +
				",activityId=" + activityId +
				",spuId=" + spuId +
				",participationMode=" + participationMode +
				",price=" + price +
				'}';
	}
}
