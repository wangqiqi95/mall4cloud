package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageValueVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序订阅模版消息类型 可选值列表
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public interface WeixinMaSubscriptTmessageValueMapper {

	/**
	 * 获取微信小程序订阅模版消息类型 可选值列表列表
	 * @return 微信小程序订阅模版消息类型 可选值列表列表
	 */
	List<WeixinMaSubscriptTmessageValue> list();

	/**
	 * 根据微信小程序订阅模版消息类型 可选值列表id获取微信小程序订阅模版消息类型 可选值列表
	 *
	 * @param id 微信小程序订阅模版消息类型 可选值列表id
	 * @return 微信小程序订阅模版消息类型 可选值列表
	 */
	WeixinMaSubscriptTmessageValue getById(@Param("id") Long id);

	/**
	 * 保存微信小程序订阅模版消息类型 可选值列表
	 * @param weixinMaSubscriptTmessageValue 微信小程序订阅模版消息类型 可选值列表
	 */
	void save(@Param("weixinMaSubscriptTmessageValue") WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue);

	void saveBatch(@Param("weixinMaSubscriptTmessageValues") List<WeixinMaSubscriptTmessageValue> weixinMaSubscriptTmessageValue);

	/**
	 * 更新微信小程序订阅模版消息类型 可选值列表
	 * @param weixinMaSubscriptTmessageValue 微信小程序订阅模版消息类型 可选值列表
	 */
	void update(@Param("weixinMaSubscriptTmessageValue") WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue);

	/**
	 * 根据微信小程序订阅模版消息类型 可选值列表id删除微信小程序订阅模版消息类型 可选值列表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据模版id查询
     * @param id
     * @return
     */
    List<WeixinMaSubscriptTmessageValueVO> getByTmessageId(@Param("id") String id);
}
