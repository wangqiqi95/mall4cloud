package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinNewsTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信图文模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
public interface WeixinNewsTemplateMapper {

	/**
	 * 获取微信图文模板表列表
	 * @return 微信图文模板表列表
	 */
	List<WeixinNewsTemplate> list();

	/**
	 * 根据微信图文模板表id获取微信图文模板表
	 *
	 * @param id 微信图文模板表id
	 * @return 微信图文模板表
	 */
	WeixinNewsTemplate getById(@Param("id") String id);

	/**
	 * 保存微信图文模板表
	 * @param weixinNewsTemplate 微信图文模板表
	 */
	void save(@Param("weixinNewsTemplate") WeixinNewsTemplate weixinNewsTemplate);

	/**
	 * 更新微信图文模板表
	 * @param weixinNewsTemplate 微信图文模板表
	 */
	void update(@Param("weixinNewsTemplate") WeixinNewsTemplate weixinNewsTemplate);

	/**
	 * 根据微信图文模板表id删除微信图文模板表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
