package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信图片模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
public interface WeixinImgTemplateMapper {

	/**
	 * 获取微信图片模板表列表
	 * @return 微信图片模板表列表
	 */
	List<WeixinImgTemplate> list();

	List<WeixinImgTemplate> listAfterThreeDayPicMediaIds();

	/**
	 * 根据微信图片模板表id获取微信图片模板表
	 *
	 * @param id 微信图片模板表id
	 * @return 微信图片模板表
	 */
	WeixinImgTemplate getById(@Param("id") String id);

	/**
	 * 保存微信图片模板表
	 * @param weixinImgTemplate 微信图片模板表
	 */
	void save(@Param("weixinImgTemplate") WeixinImgTemplate weixinImgTemplate);

	/**
	 * 更新微信图片模板表
	 * @param weixinImgTemplate 微信图片模板表
	 */
	void update(@Param("weixinImgTemplate") WeixinImgTemplate weixinImgTemplate);

	void updateMediaId(@Param("weixinImgTemplate") WeixinImgTemplate weixinImgTemplate);

	/**
	 * 根据微信图片模板表id删除微信图片模板表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
