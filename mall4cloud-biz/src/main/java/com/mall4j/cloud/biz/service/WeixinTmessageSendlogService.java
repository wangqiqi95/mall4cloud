package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import org.apache.ibatis.annotations.Param;

/**
 * 微信模板消息推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
public interface WeixinTmessageSendlogService {

	/**
	 * 分页获取微信模板消息推送日志列表
	 * @param pageDTO 分页参数
	 * @return 微信模板消息推送日志列表分页数据
	 */
	PageVO<WeixinTmessageSendlog> page(PageDTO pageDTO);

	/**
	 * 根据微信模板消息推送日志id获取微信模板消息推送日志
	 *
	 * @param id 微信模板消息推送日志id
	 * @return 微信模板消息推送日志
	 */
	WeixinTmessageSendlog getById(String id);

	/**
	 * 保存微信模板消息推送日志
	 * @param weixinTmessageSendlog 微信模板消息推送日志
	 */
	void save(WeixinTmessageSendlog weixinTmessageSendlog);

	/**
	 * 更新微信模板消息推送日志
	 * @param weixinTmessageSendlog 微信模板消息推送日志
	 */
	void update(WeixinTmessageSendlog weixinTmessageSendlog);

	/**
	 * 根据微信模板消息推送日志id删除微信模板消息推送日志
	 * @param id 微信模板消息推送日志id
	 */
	void deleteById(String id);

	void updateWxStatus(@Param("weixinTmessageSendlog") WeixinTmessageSendlog weixinTmessageSendlog);
}
