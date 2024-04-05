package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCouponShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:39
 */
public interface TCouponShopMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<TCouponShop> list();

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	TCouponShop getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param tCouponShop 
	 */
	void save(@Param("tCouponShop") TCouponShop tCouponShop);

	/**
	 * 更新
	 * @param tCouponShop 
	 */
	void update(@Param("tCouponShop") TCouponShop tCouponShop);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
