package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
public interface WeixinMaTemplateMapper {

	/**
	 * 获取微信小程序模板素材表列表
	 * @return 微信小程序模板素材表列表
	 */
	List<WeixinMaTemplate> list();

	List<WeixinMaTemplate> listAfterThreeDayPicMediaIds();

	/**
	 * 根据微信小程序模板素材表id获取微信小程序模板素材表
	 *
	 * @param id 微信小程序模板素材表id
	 * @return 微信小程序模板素材表
	 */
	WeixinMaTemplate getById(@Param("id") String id);

	/**
	 * 保存微信小程序模板素材表
	 * @param weixinMaTemplate 微信小程序模板素材表
	 */
	void save(@Param("weixinMaTemplate") WeixinMaTemplate weixinMaTemplate);

	/**
	 * 更新微信小程序模板素材表
	 * @param weixinMaTemplate 微信小程序模板素材表
	 */
	void update(@Param("weixinMaTemplate") WeixinMaTemplate weixinMaTemplate);

	void updateMediaId(@Param("weixinMaTemplate") WeixinMaTemplate weixinMaTemplate);

	/**
	 * 根据微信小程序模板素材表id删除微信小程序模板素材表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
