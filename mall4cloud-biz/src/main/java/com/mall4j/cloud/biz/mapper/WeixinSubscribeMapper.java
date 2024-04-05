package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinSubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信关注回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
public interface WeixinSubscribeMapper {

	/**
	 * 获取微信关注回复列表
	 * @return 微信关注回复列表
	 */
	List<WeixinSubscribe> list();

	/**
	 * 根据微信关注回复id获取微信关注回复
	 *
	 * @param id 微信关注回复id
	 * @return 微信关注回复
	 */
	WeixinSubscribe getById(@Param("subscribeId") Long subscribeId);

	/**
	 * 保存微信关注回复
	 * @param weixinSubscribe 微信关注回复
	 */
	void save(@Param("weixinSubscribe") WeixinSubscribe weixinSubscribe);

	/**
	 * 更新微信关注回复
	 * @param weixinSubscribe 微信关注回复
	 */
	void update(@Param("weixinSubscribe") WeixinSubscribe weixinSubscribe);

	/**
	 * 根据微信关注回复id删除微信关注回复
	 * @param id
	 */
	void deleteById(@Param("subscribeId") Long subscribeId);

	WeixinSubscribe getWeixinSubscribe(@Param("appId")String appId,@Param("replyType")Integer replyType);
}
