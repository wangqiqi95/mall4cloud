package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 优惠券设置
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public class TCoupon extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private String code;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）
     */
    private Integer kind;

    /**
     * 优惠券类型（0：抵用券/1：折扣券）
     */
    private Integer type;

    /**
     * 价格类型（0：吊牌价/1：实付金额）
     */
    private Integer priceType;

    /**
     * 抵用金额
     */
    private Long reduceAmount;

    /**
     * 折扣力度
     */
    private Double couponDiscount;

    /**
     * 金额限制类型（0：不限/1：满额）
     */
    private Integer amountLimitType;

    /**
     * 限制金额
     */
    private Double amountLimitNum;

    /**
     * 商品限制类型（0：不限/1：不超过/2：不少于）
     */
    private Integer commodityLimitType;

    /**
     * 商品限制件数
     */
    private Double commodityLimitNum;

    /**
     * 适用商品类型（0：不限/1：指定商品）
     */
    private Integer commodityScopeType;

    /**
     * 适用范围（0：不限/1：线上/2：线下）
     */
    private Integer applyScopeType;

    /**
     * 是否全部门店
     */
    private Integer isAllShop;

    /**
     * 优惠券封面
     */
    private String coverUrl;

    /**
     * 优惠券说明
     */
    private String description;

    /**
     * 优惠券备注
     */
    private String note;

    /**
     * 生效时间类型（1：固定时间/2：领取后生效）
     */
    private Integer timeType;

    /**
     * 生效开始时间
     */
    private Date startTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 领券后X天起生效
     */
    private Integer afterReceiveDays;

    /**
     * 优惠券状态（0：有效/1：失效）
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 修改人
     */
    private Long updateId;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    private Integer sourceType;

    /**
     * crm优惠券id
     */
    private String crmCouponId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public Double getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(Double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public Integer getAmountLimitType() {
		return amountLimitType;
	}

	public void setAmountLimitType(Integer amountLimitType) {
		this.amountLimitType = amountLimitType;
	}

	public Double getAmountLimitNum() {
		return amountLimitNum;
	}

	public void setAmountLimitNum(Double amountLimitNum) {
		this.amountLimitNum = amountLimitNum;
	}

	public Integer getCommodityLimitType() {
		return commodityLimitType;
	}

	public void setCommodityLimitType(Integer commodityLimitType) {
		this.commodityLimitType = commodityLimitType;
	}

	public Double getCommodityLimitNum() {
		return commodityLimitNum;
	}

	public void setCommodityLimitNum(Double commodityLimitNum) {
		this.commodityLimitNum = commodityLimitNum;
	}

	public Integer getCommodityScopeType() {
		return commodityScopeType;
	}

	public void setCommodityScopeType(Integer commodityScopeType) {
		this.commodityScopeType = commodityScopeType;
	}

	public Integer getApplyScopeType() {
		return applyScopeType;
	}

	public void setApplyScopeType(Integer applyScopeType) {
		this.applyScopeType = applyScopeType;
	}

	public Integer getIsAllShop() {
		return isAllShop;
	}

	public void setIsAllShop(Integer isAllShop) {
		this.isAllShop = isAllShop;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getAfterReceiveDays() {
		return afterReceiveDays;
	}

	public void setAfterReceiveDays(Integer afterReceiveDays) {
		this.afterReceiveDays = afterReceiveDays;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getCrmCouponId() {
		return crmCouponId;
	}

	public void setCrmCouponId(String crmCouponId) {
		this.crmCouponId = crmCouponId;
	}

	@Override
	public String toString() {
		return "TCoupon{" +
				"id=" + id +
				",code=" + code +
				",name=" + name +
				",kind=" + kind +
				",type=" + type +
				",priceType=" + priceType +
				",reduceAmount=" + reduceAmount +
				",couponDiscount=" + couponDiscount +
				",amountLimitType=" + amountLimitType +
				",amountLimitNum=" + amountLimitNum +
				",commodityLimitType=" + commodityLimitType +
				",commodityLimitNum=" + commodityLimitNum +
				",commodityScopeType=" + commodityScopeType +
				",applyScopeType=" + applyScopeType +
				",isAllShop=" + isAllShop +
				",coverUrl=" + coverUrl +
				",description=" + description +
				",note=" + note +
				",timeType=" + timeType +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",afterReceiveDays=" + afterReceiveDays +
				",status=" + status +
				",createId=" + createId +
				",createTime=" + createTime +
				",updateId=" + updateId +
				",updateTime=" + updateTime +
				",sourceType=" + sourceType +
				",crmCouponId=" + crmCouponId +
				'}';
	}
}
