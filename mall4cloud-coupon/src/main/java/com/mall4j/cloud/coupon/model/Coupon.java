package com.mall4j.cloud.coupon.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
public class Coupon extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 优惠类型 1:代金券 2:折扣券 3:兑换券
     */
    private Integer couponType;

    /**
     * 适用商品类型 0全部商品参与 1指定商品参与
     */
    private Integer suitableProdType;

    /**
     * 获取方式 0=用户领取 1=店铺发放
     */
    private Integer getWay;

    /**
     * 生效时间 1:固定时间 2:领取后生效
     */
    private Integer validTimeType;

    /**
     * 使用条件
     */
    private Long cashCondition;

    /**
     * 减免金额
     */
    private Long reduceAmount;

    /**
     * 折扣额度
     */
    private Double couponDiscount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 领券后X天起生效
     */
    private Integer afterReceiveDays;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 总库存
     */
    private Integer totalStock;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 每个用户领券上限，如不填则默认为1
     */
    private Integer limitNum;

    /**
     * 优惠券状态 0:过期 1:未过期 -1:删除
     */
    private Integer status;

    /**
     * 优惠券投放状态(-1:取消投放 0:等待投放 1:投放 2:违规下架 3:等待审核)
     */
    private Integer putonStatus;

    /**
     * 版本号
     */
    private Integer version;

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getSuitableProdType() {
		return suitableProdType;
	}

	public void setSuitableProdType(Integer suitableProdType) {
		this.suitableProdType = suitableProdType;
	}

	public Integer getGetWay() {
		return getWay;
	}

	public void setGetWay(Integer getWay) {
		this.getWay = getWay;
	}

	public Integer getValidTimeType() {
		return validTimeType;
	}

	public void setValidTimeType(Integer validTimeType) {
		this.validTimeType = validTimeType;
	}

	public Long getCashCondition() {
		return cashCondition;
	}

	public void setCashCondition(Long cashCondition) {
		this.cashCondition = cashCondition;
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

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public Integer getStocks() {
		return stocks;
	}

	public void setStocks(Integer stocks) {
		this.stocks = stocks;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPutonStatus() {
		return putonStatus;
	}

	public void setPutonStatus(Integer putonStatus) {
		this.putonStatus = putonStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Coupon{" +
				"couponId=" + couponId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",couponName=" + couponName +
				",subTitle=" + subTitle +
				",couponType=" + couponType +
				",suitableProdType=" + suitableProdType +
				",getWay=" + getWay +
				",validTimeType=" + validTimeType +
				",cashCondition=" + cashCondition +
				",reduceAmount=" + reduceAmount +
				",couponDiscount=" + couponDiscount +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",afterReceiveDays=" + afterReceiveDays +
				",validDays=" + validDays +
				",totalStock=" + totalStock +
				",stocks=" + stocks +
				",limitNum=" + limitNum +
				",status=" + status +
				",putonStatus=" + putonStatus +
				",version=" + version +
				'}';
	}
}
