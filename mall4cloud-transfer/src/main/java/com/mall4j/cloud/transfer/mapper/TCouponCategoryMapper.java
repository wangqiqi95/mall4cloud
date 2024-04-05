package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCouponCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public interface TCouponCategoryMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<TCouponCategory> list();

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	TCouponCategory getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param tCouponCategory 
	 */
	void save(@Param("tCouponCategory") TCouponCategory tCouponCategory);

	/**
	 * 更新
	 * @param tCouponCategory 
	 */
	void update(@Param("tCouponCategory") TCouponCategory tCouponCategory);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
