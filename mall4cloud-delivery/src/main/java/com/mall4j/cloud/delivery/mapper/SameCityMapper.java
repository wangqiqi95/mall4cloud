package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.model.SameCity;
import org.apache.ibatis.annotations.Param;

/**
 * 同城配送信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface SameCityMapper {

	/**
	 * 保存同城配送信息
	 *
	 * @param sameCity 同城配送信息
	 */
	void save(@Param("sameCity") SameCity sameCity);

	/**
	 * 更新同城配送信息
	 *
	 * @param sameCity 同城配送信息
	 */
	void updateByShopId(@Param("sameCity") SameCity sameCity);

	/**
	 * 根据店铺id获取同城配送信息
	 *
	 * @param shopId 店铺id
	 * @return 同城配送信息
	 */
    SameCity getByShopId(@Param("shopId") Long shopId);
}
