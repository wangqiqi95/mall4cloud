package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.constant.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 购物车VO
 *
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
@Data
public class ShopCartVO {

	@ApiModelProperty(value = "店铺ID", required = true)
	private Long shopId = Constant.MAIN_SHOP;

	@ApiModelProperty(value = "店铺名称", required = true)
	private String shopName;

	@ApiModelProperty("店铺类型1自营店 2普通店")
	private Integer shopType;

	@ApiModelProperty(value = "购物车满减活动携带的商品", required = true)
	private List<ShopCartItemDiscountVO> shopCartItemDiscounts;

	@ApiModelProperty(value = "店铺优惠金额(促销活动 + 优惠券 + 积分优惠金额 + 其他)", required = true)
	private Long shopReduce;

	@ApiModelProperty(value = "促销活动优惠金额", required = true)
	private Long discountReduce;

	@ApiModelProperty(value = "实际总值(商品总值 - 优惠)", required = true)
	private Long actualTotal;

	@ApiModelProperty(value = "商品总值", required = true)
	private Long total;

	@ApiModelProperty(value = "商品积分总值")
	private Long scoreTotal;

	@ApiModelProperty(value = "整个店铺可以使用的优惠券列表", required = true)
	private List<CouponOrderVO> coupons;

	@ApiModelProperty(value = "优惠券优惠金额", required = true)
	private Long couponReduce;

	@ApiModelProperty(value = "数量", required = true)
	private Integer totalCount;

	@ApiModelProperty(value = "运费", required = true)
	private Long transfee;

	@ApiModelProperty(value = "等级免运费金额", required = true)
	private Long freeTransfee;

	private String styleCode;

	public Long getFreeTransfee() {
		return freeTransfee;
	}

	public void setFreeTransfee(Long freeTransfee) {
		this.freeTransfee = freeTransfee;
	}

	public Long getTransfee() {
		return transfee;
	}

	public void setTransfee(Long transfee) {
		this.transfee = transfee;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<ShopCartItemDiscountVO> getShopCartItemDiscounts() {
		return shopCartItemDiscounts;
	}

	public void setShopCartItemDiscounts(List<ShopCartItemDiscountVO> shopCartItemDiscounts) {
		this.shopCartItemDiscounts = shopCartItemDiscounts;
	}

	public Long getShopReduce() {
		return shopReduce;
	}

	public void setShopReduce(Long shopReduce) {
		this.shopReduce = shopReduce;
	}

	public Long getDiscountReduce() {
		return discountReduce;
	}

	public void setDiscountReduce(Long discountReduce) {
		this.discountReduce = discountReduce;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getCouponReduce() {
		return couponReduce;
	}

	public void setCouponReduce(Long couponReduce) {
		this.couponReduce = couponReduce;
	}

	public List<CouponOrderVO> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponOrderVO> coupons) {
		this.coupons = coupons;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Long getScoreTotal() {
		return scoreTotal;
	}

	public void setScoreTotal(Long scoreTotal) {
		this.scoreTotal = scoreTotal;
	}

	@Override
	public String toString() {
		return "ShopCartVO{" +
				"shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", shopType=" + shopType +
				", shopCartItemDiscounts=" + shopCartItemDiscounts +
				", shopReduce=" + shopReduce +
				", discountReduce=" + discountReduce +
				", actualTotal=" + actualTotal +
				", total=" + total +
				", scoreTotal=" + scoreTotal +
				", coupons=" + coupons +
				", couponReduce=" + couponReduce +
				", totalCount=" + totalCount +
				", transfee=" + transfee +
				", freeTransfee=" + freeTransfee +
				'}';
	}
}
