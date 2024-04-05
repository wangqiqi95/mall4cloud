package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinTextTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信文本模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
public interface WeixinTextTemplateMapper {

	/**
	 * 获取微信文本模板表列表
	 * @return 微信文本模板表列表
	 */
	List<WeixinTextTemplate> list();

	/**
	 * 根据微信文本模板表id获取微信文本模板表
	 *
	 * @param id 微信文本模板表id
	 * @return 微信文本模板表
	 */
	WeixinTextTemplate getById(@Param("id") String id);

	/**
	 * 保存微信文本模板表
	 * @param weixinTextTemplate 微信文本模板表
	 */
	void save(@Param("weixinTextTemplate") WeixinTextTemplate weixinTextTemplate);

	/**
	 * 更新微信文本模板表
	 * @param weixinTextTemplate 微信文本模板表
	 */
	void update(@Param("weixinTextTemplate") WeixinTextTemplate weixinTextTemplate);

	/**
	 * 根据微信文本模板表id删除微信文本模板表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
