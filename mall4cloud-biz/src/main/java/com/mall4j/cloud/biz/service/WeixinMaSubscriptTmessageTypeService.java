package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageType;

/**
 * 微信小程序订阅模版消息类型
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:44
 */
public interface WeixinMaSubscriptTmessageTypeService {

	/**
	 * 分页获取微信小程序订阅模版消息类型列表
	 * @param pageDTO 分页参数
	 * @return 微信小程序订阅模版消息类型列表分页数据
	 */
	PageVO<WeixinMaSubscriptTmessageType> page(PageDTO pageDTO);

	/**
	 * 根据微信小程序订阅模版消息类型id获取微信小程序订阅模版消息类型
	 *
	 * @param id 微信小程序订阅模版消息类型id
	 * @return 微信小程序订阅模版消息类型
	 */
	WeixinMaSubscriptTmessageType getById(Long id);

	/**
	 * 保存微信小程序订阅模版消息类型
	 * @param weixinMaSubscriptTmessageType 微信小程序订阅模版消息类型
	 */
	void save(WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType);

	/**
	 * 更新微信小程序订阅模版消息类型
	 * @param weixinMaSubscriptTmessageType 微信小程序订阅模版消息类型
	 */
	void update(WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType);

	/**
	 * 根据微信小程序订阅模版消息类型id删除微信小程序订阅模版消息类型
	 * @param id 微信小程序订阅模版消息类型id
	 */
	void deleteById(Long id);
}
