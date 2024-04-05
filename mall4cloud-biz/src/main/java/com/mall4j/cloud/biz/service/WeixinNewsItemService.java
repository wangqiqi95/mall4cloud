package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinNewsItem;

/**
 * 微信图文模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
public interface WeixinNewsItemService {

	/**
	 * 分页获取微信图文模板素材表列表
	 * @param pageDTO 分页参数
	 * @return 微信图文模板素材表列表分页数据
	 */
	PageVO<WeixinNewsItem> page(PageDTO pageDTO);

	/**
	 * 根据微信图文模板素材表id获取微信图文模板素材表
	 *
	 * @param id 微信图文模板素材表id
	 * @return 微信图文模板素材表
	 */
	WeixinNewsItem getById(Long id);

	/**
	 * 保存微信图文模板素材表
	 * @param weixinNewsItem 微信图文模板素材表
	 */
	void save(WeixinNewsItem weixinNewsItem);

	/**
	 * 更新微信图文模板素材表
	 * @param weixinNewsItem 微信图文模板素材表
	 */
	void update(WeixinNewsItem weixinNewsItem);

	/**
	 * 根据微信图文模板素材表id删除微信图文模板素材表
	 * @param id 微信图文模板素材表id
	 */
	void deleteById(Long id);
}
