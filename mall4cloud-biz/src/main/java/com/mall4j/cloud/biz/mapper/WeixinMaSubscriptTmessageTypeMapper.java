package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序订阅模版消息类型
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public interface WeixinMaSubscriptTmessageTypeMapper {

	/**
	 * 获取微信小程序订阅模版消息类型列表
	 * @return 微信小程序订阅模版消息类型列表
	 */
	List<WeixinMaSubscriptTmessageType> list();

	/**
	 * 根据微信小程序订阅模版消息类型id获取微信小程序订阅模版消息类型
	 *
	 * @param id 微信小程序订阅模版消息类型id
	 * @return 微信小程序订阅模版消息类型
	 */
	WeixinMaSubscriptTmessageType getById(@Param("id") Long id);

	/**
	 * 保存微信小程序订阅模版消息类型
	 * @param weixinMaSubscriptTmessageType 微信小程序订阅模版消息类型
	 */
	void save(@Param("weixinMaSubscriptTmessageType") WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType);

	/**
	 * 更新微信小程序订阅模版消息类型
	 * @param weixinMaSubscriptTmessageType 微信小程序订阅模版消息类型
	 */
	void update(@Param("weixinMaSubscriptTmessageType") WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType);

	/**
	 * 根据微信小程序订阅模版消息类型id删除微信小程序订阅模版消息类型
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
