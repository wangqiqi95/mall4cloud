package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 积分限时折扣兑换券
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
public class ScoreTimeDiscountActivityItem  implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 积分折扣活动id
     */
    private Long activityId;

    /**
     * 积分活动ID
     */
    private Long convertId;

    /**
     * 折扣百分比
     */
    private Integer discount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getConvertId() {
		return convertId;
	}

	public void setConvertId(Long convertId) {
		this.convertId = convertId;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "ScoreTimeDiscountActivityItem{" +
				"id=" + id +
				",activityId=" + activityId +
				",convertId=" + convertId +
				",discount=" + discount +
				'}';
	}
}
