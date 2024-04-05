package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinTmessage;
import com.mall4j.cloud.biz.vo.WeixinTmessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信消息模板
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
public interface WeixinTmessageMapper {

	/**
	 * 获取微信消息模板列表
	 * @return 微信消息模板列表
	 */
	List<WeixinTmessage> list();

	List<WeixinTmessageVO> getList(@Param("appId") String appId,@Param("dataSrc") Integer dataSrc);

	/**
	 * 根据微信消息模板id获取微信消息模板
	 *
	 * @param id 微信消息模板id
	 * @return 微信消息模板
	 */
	WeixinTmessage getById(@Param("id") String id);

	/**
	 * 保存微信消息模板
	 * @param weixinTmessage 微信消息模板
	 */
	void save(@Param("weixinTmessage") WeixinTmessage weixinTmessage);

	/**
	 * 更新微信消息模板
	 * @param weixinTmessage 微信消息模板
	 */
	void update(@Param("weixinTmessage") WeixinTmessage weixinTmessage);

	/**
	 * 根据微信消息模板id删除微信消息模板
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
