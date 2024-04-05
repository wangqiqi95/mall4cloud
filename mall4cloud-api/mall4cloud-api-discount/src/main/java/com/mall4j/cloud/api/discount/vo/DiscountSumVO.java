package com.mall4j.cloud.api.discount.vo;

import com.mall4j.cloud.common.order.vo.ChooseDiscountItemVO;

import java.io.Serializable;
import java.util.Map;

/**
 * 某个店铺的满江活动的活动信息
 * @author FrozenWatermelon
 */
public class DiscountSumVO implements Serializable {

	/**
	 * 以优惠活动id为key， 优惠活动金额 为value的map
	 * {优惠活动id：优惠活动金额}
	 */
	private Map<Long, DiscountSumItemVO> discountIdDiscountSumItemMap;

	/**
	 * 所有优惠活动加起来的金额
	 */
	private Long totalReduceAmount;

	private ChooseDiscountItemVO chooseDiscountItem;

	public Map<Long, DiscountSumItemVO> getDiscountIdDiscountSumItemMap() {
		return discountIdDiscountSumItemMap;
	}

	public void setDiscountIdDiscountSumItemMap(Map<Long, DiscountSumItemVO> discountIdDiscountSumItemMap) {
		this.discountIdDiscountSumItemMap = discountIdDiscountSumItemMap;
	}

	public Long getTotalReduceAmount() {
		return totalReduceAmount;
	}

	public void setTotalReduceAmount(Long totalReduceAmount) {
		this.totalReduceAmount = totalReduceAmount;
	}


	@Override
	public String toString() {
		return "DiscountSumVO{" +
				"discountIdDiscountSumItemMap=" + discountIdDiscountSumItemMap +
				", totalReduceAmount=" + totalReduceAmount +
				", chooseDiscountItem=" + chooseDiscountItem +
				'}';
	}
}
