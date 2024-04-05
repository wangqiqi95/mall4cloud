package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 问卷奖品清单 实物奖品维护DTO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public class QuestionnaireGiftDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("礼品类型 0积分 1优惠券 2抽奖 3实物")
    private Integer giftType;

    @ApiModelProperty("奖品 积分直接维护赠送的积分值 优惠券维护赠送的优惠券id 抽奖维护抽奖活动id")
    private Long giftId;

    @ApiModelProperty("奖品名称")
    private String name;

    @ApiModelProperty("奖品图片")
    private String pic;

    @ApiModelProperty("库存")
    private Integer stock;

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

	public Integer getGiftType() {
		return giftType;
	}

	public void setGiftType(Integer giftType) {
		this.giftType = giftType;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "QuestionnaireGiftDTO{" +
				"id=" + id +
				",activityId=" + activityId +
				",giftType=" + giftType +
				",giftId=" + giftId +
				",name=" + name +
				",pic=" + pic +
				",stock=" + stock +
				'}';
	}
}
