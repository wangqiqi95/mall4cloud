package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTextTemplate;

/**
 * 微信文本模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
public interface WeixinTextTemplateService {

	/**
	 * 分页获取微信文本模板表列表
	 * @param pageDTO 分页参数
	 * @return 微信文本模板表列表分页数据
	 */
	PageVO<WeixinTextTemplate> page(PageDTO pageDTO);

	/**
	 * 根据微信文本模板表id获取微信文本模板表
	 *
	 * @param id 微信文本模板表id
	 * @return 微信文本模板表
	 */
	WeixinTextTemplate getById(String id);

	/**
	 * 保存微信文本模板表
	 * @param weixinTextTemplate 微信文本模板表
	 */
	void save(WeixinTextTemplate weixinTextTemplate);

	/**
	 * 更新微信文本模板表
	 * @param weixinTextTemplate 微信文本模板表
	 */
	void update(WeixinTextTemplate weixinTextTemplate);

	/**
	 * 根据微信文本模板表id删除微信文本模板表
	 * @param id 微信文本模板表id
	 */
	void deleteById(String id);
}
