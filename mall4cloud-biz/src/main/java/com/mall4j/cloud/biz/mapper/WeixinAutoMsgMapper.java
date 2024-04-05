package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinAutoMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信消息自动回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:52:24
 */
public interface WeixinAutoMsgMapper {

	/**
	 * 获取微信消息自动回复列表
	 * @return 微信消息自动回复列表
	 */
	List<WeixinAutoMsg> list();

	/**
	 * 根据微信消息自动回复id获取微信消息自动回复
	 *
	 * @param id 微信消息自动回复id
	 * @return 微信消息自动回复
	 */
	WeixinAutoMsg getById(@Param("id") Long id);

	/**
	 * 保存微信消息自动回复
	 * @param weixinAutoMsg 微信消息自动回复
	 */
	void save(@Param("weixinAutoMsg") WeixinAutoMsg weixinAutoMsg);

	/**
	 * 更新微信消息自动回复
	 * @param weixinAutoMsg 微信消息自动回复
	 */
	void update(@Param("weixinAutoMsg") WeixinAutoMsg weixinAutoMsg);

	/**
	 * 根据微信消息自动回复id删除微信消息自动回复
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	WeixinAutoMsg getWeixinAutoMsg(@Param("appId") String appId);
}
