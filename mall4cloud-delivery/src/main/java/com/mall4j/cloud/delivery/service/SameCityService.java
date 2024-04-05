package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.delivery.model.SameCity;

import java.util.List;

/**
 * 同城配送信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface SameCityService {

	/**
	 * 根据店铺id获取同城配送信息
	 *
	 * @param shopId 同城配送信息id
	 * @return 同城配送信息
	 */
	SameCity getSameCityByShopId(Long shopId);

	/**
	 * 保存同城配送信息
	 * @param sameCity 同城配送信息
	 */
	void save(SameCity sameCity);

	/**
	 * 更新同城配送信息
	 * @param sameCity 同城配送信息
	 */
	void updateByShopId(SameCity sameCity);

	/**
	 * 移除缓存
	 *
	 * @param shopId 同城配送信息id
	 * @return 同城配送信息
	 */
	void removeSameCityCacheByShopId(Long shopId);

	/**
	 * 计算同城配送的运费
	 * @param shopCartItems 订单项
	 * @param userAddr 用户地址
	 * @return 运费
	 */
    long calculateTransFee(List<ShopCartItemVO> shopCartItems, UserAddrVO userAddr);
}
