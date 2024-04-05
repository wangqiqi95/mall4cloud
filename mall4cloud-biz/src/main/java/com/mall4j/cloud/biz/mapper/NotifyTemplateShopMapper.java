package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.NotifyTemplateShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyTemplateShopMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<NotifyTemplateShop> list();

	/**
	 * 根据id获取
	 *
	 * @param notifyShopId id
	 * @return 
	 */
	NotifyTemplateShop getByNotifyShopId(@Param("notifyShopId") Long notifyShopId);

	/**
	 * 保存
	 * @param notifyTemplateShop 
	 */
	void save(@Param("notifyTemplateShop") NotifyTemplateShop notifyTemplateShop);

	/**
	 * 更新
	 * @param notifyTemplateShop 
	 */
	void update(@Param("notifyTemplateShop") NotifyTemplateShop notifyTemplateShop);

	/**
	 * 根据id删除
	 * @param notifyShopId
	 */
	void deleteById(@Param("notifyShopId") Long notifyShopId);
}
