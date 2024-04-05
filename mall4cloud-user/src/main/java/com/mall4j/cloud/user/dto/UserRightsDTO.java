package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用户权益信息DTO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserRightsDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权益id")
    private Long rightsId;

    @ApiModelProperty("权益名称")
    private String rightsName;

    @ApiModelProperty("权益图标")
    private String icon;

    @ApiModelProperty("权益简介")
    private String description;

	@ApiModelProperty("权益详情")
	private String details;

    @ApiModelProperty("状态：-1: 删除 0:禁用 1：正常(仅用于系统核销)")
    private Integer status;

    @ApiModelProperty("排序 从小到大")
    private Integer seq;

    @ApiModelProperty("权益类型[0.自定义 1.积分回馈倍率 2.优惠券 3.积分赠送(数量) 4.会员折扣 5.包邮类型]")
    private Integer rightsType;

    @ApiModelProperty("积分回馈倍率")
    private Integer rateScore;

    @ApiModelProperty("赠送积分")
    private Long presScore;

    @ApiModelProperty("会员折扣")
    private Integer discount;

    @ApiModelProperty("折扣范围[0.全平台 1.自营店]")
    private Integer discountRange;

    @ApiModelProperty("包邮类型[1.全平台包邮 2.自营店包邮]")
    private Integer freeFeeType;

	@ApiModelProperty("优惠券id列表")
	private List<Long> couponIds;

	public Long getRightsId() {
		return rightsId;
	}

	public void setRightsId(Long rightsId) {
		this.rightsId = rightsId;
	}

	public String getRightsName() {
		return rightsName;
	}

	public void setRightsName(String rightsName) {
		this.rightsName = rightsName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getRightsType() {
		return rightsType;
	}

	public void setRightsType(Integer rightsType) {
		this.rightsType = rightsType;
	}

	public Integer getRateScore() {
		return rateScore;
	}

	public void setRateScore(Integer rateScore) {
		this.rateScore = rateScore;
	}

	public Long getPresScore() {
		return presScore;
	}

	public void setPresScore(Long presScore) {
		this.presScore = presScore;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getDiscountRange() {
		return discountRange;
	}

	public void setDiscountRange(Integer discountRange) {
		this.discountRange = discountRange;
	}

	public Integer getFreeFeeType() {
		return freeFeeType;
	}

	public void setFreeFeeType(Integer freeFeeType) {
		this.freeFeeType = freeFeeType;
	}

	public List<Long> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Long> couponIds) {
		this.couponIds = couponIds;
	}

	@Override
	public String toString() {
		return "UserRightsDTO{" +
				"rightsId=" + rightsId +
				",rightsName=" + rightsName +
				",icon=" + icon +
				",description=" + description +
				",details=" + details +
				",status=" + status +
				",seq=" + seq +
				",rightsType=" + rightsType +
				",rateScore=" + rateScore +
				",presScore=" + presScore +
				",discount=" + discount +
				",discountRange=" + discountRange +
				",freeFeeType=" + freeFeeType +
				",couponIds=" + couponIds +
				'}';
	}
}
