package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessage;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序订阅模版消息
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public interface WeixinMaSubscriptTmessageMapper {

	/**
	 * 获取微信小程序订阅模版消息列表
	 * @return 微信小程序订阅模版消息列表
	 */
	List<WeixinMaSubscriptTmessage> list(@Param("appId") String appId,
										 @Param("businessType") Integer businessType,
										 @Param("sendType") Integer sendType);

	List<WeixinMaSubscriptTmessageTypeVO> getList(@Param("appId") String appId,
												  @Param("businessType") Integer businessType,
												  @Param("sendType") Integer sendType);

	/**
	 * 根据微信小程序订阅模版消息id获取微信小程序订阅模版消息
	 *
	 * @param id 微信小程序订阅模版消息id
	 * @return 微信小程序订阅模版消息
	 */
	WeixinMaSubscriptTmessage getById(@Param("id") String id);

	/**
	 * 保存微信小程序订阅模版消息
	 * @param weixinMaSubscriptTmessage 微信小程序订阅模版消息
	 */
	void save(@Param("weixinMaSubscriptTmessage") WeixinMaSubscriptTmessage weixinMaSubscriptTmessage);

	/**
	 * 更新微信小程序订阅模版消息
	 * @param weixinMaSubscriptTmessage 微信小程序订阅模版消息
	 */
	void update(@Param("weixinMaSubscriptTmessage") WeixinMaSubscriptTmessage weixinMaSubscriptTmessage);

	/**
	 * 根据微信小程序订阅模版消息id删除微信小程序订阅模版消息
	 * @param id
	 */
	void deleteById(@Param("id") String id);

    /**
     * 根据消息类型查询模版消息
     * @param sendType
     * @return
     */
    WeixinMaSubscriptTmessageVO getBySendType(@Param("sendType") Integer sendType);

    /**
     * 根据模版分类查询模版消息
     * @param type
     * @return
     */
    List<WeixinMaSubscriptTmessageVO> getByType(@Param("type") Integer type);
}
