package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信公共回复内容表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:03
 */
public interface WeixinAutoResponseMapper {

	/**
	 * 获取微信公共回复内容表列表
	 * @return 微信公共回复内容表列表
	 */
	List<WeixinAutoResponse> list();

	/**
	 * 根据微信公共回复内容表id获取微信公共回复内容表
	 *
	 * @param id 微信公共回复内容表id
	 * @return 微信公共回复内容表
	 */
	WeixinAutoResponse getById(@Param("id") Long id);

	/**
	 * 保存微信公共回复内容表
	 * @param weixinAutoResponse 微信公共回复内容表
	 */
	void save(@Param("weixinAutoResponse") WeixinAutoResponse weixinAutoResponse);

	/**
	 * 更新微信公共回复内容表
	 * @param weixinAutoResponse 微信公共回复内容表
	 */
	void update(@Param("weixinAutoResponse") WeixinAutoResponse weixinAutoResponse);

	/**
	 * 根据微信公共回复内容表id删除微信公共回复内容表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByDataId(@Param("dataId") String dataId);

	WeixinAutoResponse getWeixinAuto(@Param("dataId") String dataId,
									 @Param("dataSrc") Integer dataSrc,
									 @Param("msgType") String msgType);

	WeixinAutoResponse getWeixinAutoByTemplateId(@Param("dataId") String dataId,
									 @Param("dataSrc") Integer dataSrc,
									 @Param("msgType") String msgType,
									 @Param("templateId") String templateId);

	List<WeixinAutoResponse> getWeixinAutos(@Param("dataId") String dataId,
									 @Param("dataSrc") Integer dataSrc);
}
