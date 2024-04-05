package com.mall4j.cloud.api.discount.vo;

import java.io.Serializable;

/**
 * 每个满减活动的活动信息
 * @author FrozenWatermelon
 */
public class DiscountSumItemVO implements Serializable {

	/**
	 * 需要商品金额/数量
	 */
	private Long needAmount;

	/**
	 * 优惠项
	 */
	private Long discountItemId;

	/**
	 * 优惠（元/折）
	 */
	private Long discount;

	/**
	 * 该满减活动实际优惠金额
	 * 如果活动优惠金额有上限，且优惠金额达到了上限，
	 * 那么这个金额就是活动优惠金额的上限
	 */
	private Long reduceAmount;

	/**
	 * 参与满减满折优惠的商品数量
	 */
	private Integer count;
	/**
	 * 参与满减满折优惠的商品金额
	 */
	private Long prodsPrice;

	public Long getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(Long needAmount) {
		this.needAmount = needAmount;
	}

	public Long getDiscountItemId() {
		return discountItemId;
	}

	public void setDiscountItemId(Long discountItemId) {
		this.discountItemId = discountItemId;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getProdsPrice() {
		return prodsPrice;
	}

	public void setProdsPrice(Long prodsPrice) {
		this.prodsPrice = prodsPrice;
	}

	@Override
	public String toString() {
		return "DiscountSumItemVO{" +
				"needAmount=" + needAmount +
				", discountItemId=" + discountItemId +
				", discount=" + discount +
				", reduceAmount=" + reduceAmount +
				", count=" + count +
				", prodsPrice=" + prodsPrice +
				'}';
	}
}
