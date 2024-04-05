package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageOptionalValue;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageOptionalValueVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序订阅模版消息 值
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public interface WeixinMaSubscriptTmessageOptionalValueMapper {

	/**
	 * 获取微信小程序订阅模版消息 值列表
	 * @return 微信小程序订阅模版消息 值列表
	 */
	List<WeixinMaSubscriptTmessageOptionalValue> list();

	/**
	 * 根据微信小程序订阅模版消息 值id获取微信小程序订阅模版消息 值
	 *
	 * @param id 微信小程序订阅模版消息 值id
	 * @return 微信小程序订阅模版消息 值
	 */
	WeixinMaSubscriptTmessageOptionalValue getById(@Param("id") Long id);

	/**
	 * 保存微信小程序订阅模版消息 值
	 * @param weixinMaSubscriptTmessageOptionalValue 微信小程序订阅模版消息 值
	 */
	void save(@Param("weixinMaSubscriptTmessageOptionalValue") WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue);

	/**
	 * 更新微信小程序订阅模版消息 值
	 * @param weixinMaSubscriptTmessageOptionalValue 微信小程序订阅模版消息 值
	 */
	void update(@Param("weixinMaSubscriptTmessageOptionalValue") WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue);

	/**
	 * 根据微信小程序订阅模版消息 值id删除微信小程序订阅模版消息 值
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    List<WeixinMaSubscriptTmessageOptionalValueVO> getByTypeId(@Param("typeId")Integer typeId);
}
