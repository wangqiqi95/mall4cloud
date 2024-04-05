package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinNewsTemplate;

/**
 * 微信图文模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
public interface WeixinNewsTemplateService {

	/**
	 * 分页获取微信图文模板表列表
	 * @param pageDTO 分页参数
	 * @return 微信图文模板表列表分页数据
	 */
	PageVO<WeixinNewsTemplate> page(PageDTO pageDTO);

	/**
	 * 根据微信图文模板表id获取微信图文模板表
	 *
	 * @param id 微信图文模板表id
	 * @return 微信图文模板表
	 */
	WeixinNewsTemplate getById(String id);

	/**
	 * 保存微信图文模板表
	 * @param weixinNewsTemplate 微信图文模板表
	 */
	void save(WeixinNewsTemplate weixinNewsTemplate);

	/**
	 * 更新微信图文模板表
	 * @param weixinNewsTemplate 微信图文模板表
	 */
	void update(WeixinNewsTemplate weixinNewsTemplate);

	/**
	 * 根据微信图文模板表id删除微信图文模板表
	 * @param id 微信图文模板表id
	 */
	void deleteById(String id);
}
