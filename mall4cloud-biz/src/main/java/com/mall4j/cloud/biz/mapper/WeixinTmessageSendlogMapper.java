package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信模板消息推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
public interface WeixinTmessageSendlogMapper {

	/**
	 * 获取微信模板消息推送日志列表
	 * @return 微信模板消息推送日志列表
	 */
	List<WeixinTmessageSendlog> list();

	/**
	 * 根据微信模板消息推送日志id获取微信模板消息推送日志
	 *
	 * @param id 微信模板消息推送日志id
	 * @return 微信模板消息推送日志
	 */
	WeixinTmessageSendlog getById(@Param("id") String id);

	/**
	 * 保存微信模板消息推送日志
	 * @param weixinTmessageSendlog 微信模板消息推送日志
	 */
	void save(@Param("weixinTmessageSendlog") WeixinTmessageSendlog weixinTmessageSendlog);

	/**
	 * 更新微信模板消息推送日志
	 * @param weixinTmessageSendlog 微信模板消息推送日志
	 */
	void update(@Param("weixinTmessageSendlog") WeixinTmessageSendlog weixinTmessageSendlog);

	void updateWxStatus(@Param("weixinTmessageSendlog") WeixinTmessageSendlog weixinTmessageSendlog);

	/**
	 * 根据微信模板消息推送日志id删除微信模板消息推送日志
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
