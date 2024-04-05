package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 用户权益信息
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserRights extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 权益id
     */
    private Long rightsId;

    /**
     * 权益名称
     */
    private String rightsName;

    /**
     * 权益图标
     */
    private String icon;

    /**
     * 权益简介
     */
    private String description;

	/**
	 * 权益详情
	 */
	private String details;

	/**
     * 状态：-1: 删除 0:禁用 1：正常(仅用于系统核销)
     */
    private Integer status;

    /**
     * 排序 从小到大
     */
    private Integer seq;

    /**
     * 权益类型[0.自定义 1.积分回馈倍率 2.优惠券 3.积分赠送(数量) 4.会员折扣 5.包邮类型]
     */
    private Integer rightsType;

    /**
     * 积分回馈倍率
     */
    private Integer rateScore;

    /**
     * 赠送积分
     */
    private Long presScore;

    /**
     * 会员折扣
     */
    private Integer discount;

    /**
     * 折扣范围[0.全平台 1.自营店]
     */
    private Integer discountRange;

    /**
     * 包邮类型[1.全平台包邮 2.自营店包邮]
     */
    private Integer freeFeeType;

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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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

	@Override
	public String toString() {
		return "UserRights{" +
				"rightsId=" + rightsId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
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
				'}';
	}
}
