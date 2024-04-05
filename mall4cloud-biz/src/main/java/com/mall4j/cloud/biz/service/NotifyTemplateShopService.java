package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.NotifyTemplateShop;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyTemplateShopService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<NotifyTemplateShop> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param notifyShopId id
	 * @return 
	 */
	NotifyTemplateShop getByNotifyShopId(Long notifyShopId);

	/**
	 * 保存
	 * @param notifyTemplateShop 
	 */
	void save(NotifyTemplateShop notifyTemplateShop);

	/**
	 * 更新
	 * @param notifyTemplateShop 
	 */
	void update(NotifyTemplateShop notifyTemplateShop);

	/**
	 * 根据id删除
	 * @param notifyShopId id
	 */
	void deleteById(Long notifyShopId);
}
