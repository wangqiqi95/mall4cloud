package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 优惠券VO
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
public class CouponVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("副标题")
    private String subTitle;

    @ApiModelProperty("优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty("适用商品类型 0全部商品参与 1指定商品参与")
    private Integer suitableProdType;

    @ApiModelProperty("获取方式 0=用户领取 1=店铺发放")
    private Integer getWay;

    @ApiModelProperty("生效时间 1:固定时间 2:领取后生效")
    private Integer validTimeType;

    @ApiModelProperty("使用条件")
    private Long cashCondition;

    @ApiModelProperty("减免金额")
    private Long reduceAmount;

    @ApiModelProperty("折扣额度")
    private Double couponDiscount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("领券后X天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty("有效天数")
    private Integer validDays;

    @ApiModelProperty("总库存")
    private Integer totalStock;

    @ApiModelProperty("库存")
    private Integer stocks;

    @ApiModelProperty("每个用户领券上限，如不填则默认为1")
    private Integer limitNum;

    @ApiModelProperty("优惠券状态 0:过期 1:未过期")
    private Integer status;

    @ApiModelProperty("优惠券投放状态(-1:取消投放 0:等待投放 1:投放 2:违规下架 3:等待审核)")
    private Integer putonStatus;

	@ApiModelProperty("商品Id列表")
	private List<Long> spuIds;

    @ApiModelProperty("商品列表")
	private List<SpuSearchVO> spus;

    @ApiModelProperty("用户拥有的优惠券数量")
	private Integer couponCount;

	@ApiModelProperty(value = "领券时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date receiveTime;

	@ApiModelProperty("店铺名称")
	private String shopName;

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

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public List<SpuSearchVO> getSpus() {
		return spus;
	}

	public void setSpus(List<SpuSearchVO> spus) {
		this.spus = spus;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Override
	public String toString() {
		return "CouponVO{" +
				"couponId=" + couponId +
				", shopId=" + shopId +
				", couponName='" + couponName + '\'' +
				", subTitle='" + subTitle + '\'' +
				", couponType=" + couponType +
				", suitableProdType=" + suitableProdType +
				", getWay=" + getWay +
				", validTimeType=" + validTimeType +
				", cashCondition=" + cashCondition +
				", reduceAmount=" + reduceAmount +
				", couponDiscount=" + couponDiscount +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", afterReceiveDays=" + afterReceiveDays +
				", validDays=" + validDays +
				", totalStock=" + totalStock +
				", stocks=" + stocks +
				", limitNum=" + limitNum +
				", status=" + status +
				", putonStatus=" + putonStatus +
				", spuIds=" + spuIds +
				", spus=" + spus +
				", couponCount=" + couponCount +
				", receiveTime=" + receiveTime +
				", shopName=" + shopName +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
