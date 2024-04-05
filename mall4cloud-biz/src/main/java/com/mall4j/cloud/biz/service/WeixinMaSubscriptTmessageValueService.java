package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageValue;

/**
 * 微信小程序订阅模版消息类型 可选值列表
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:45
 */
public interface WeixinMaSubscriptTmessageValueService {

	/**
	 * 分页获取微信小程序订阅模版消息类型 可选值列表列表
	 * @param pageDTO 分页参数
	 * @return 微信小程序订阅模版消息类型 可选值列表列表分页数据
	 */
	PageVO<WeixinMaSubscriptTmessageValue> page(PageDTO pageDTO);

	/**
	 * 根据微信小程序订阅模版消息类型 可选值列表id获取微信小程序订阅模版消息类型 可选值列表
	 *
	 * @param id 微信小程序订阅模版消息类型 可选值列表id
	 * @return 微信小程序订阅模版消息类型 可选值列表
	 */
	WeixinMaSubscriptTmessageValue getById(Long id);

	/**
	 * 保存微信小程序订阅模版消息类型 可选值列表
	 * @param weixinMaSubscriptTmessageValue 微信小程序订阅模版消息类型 可选值列表
	 */
	void save(WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue);

	/**
	 * 更新微信小程序订阅模版消息类型 可选值列表
	 * @param weixinMaSubscriptTmessageValue 微信小程序订阅模版消息类型 可选值列表
	 */
	void update(WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue);

	/**
	 * 根据微信小程序订阅模版消息类型 可选值列表id删除微信小程序订阅模版消息类型 可选值列表
	 * @param id 微信小程序订阅模版消息类型 可选值列表id
	 */
	void deleteById(Long id);
}
