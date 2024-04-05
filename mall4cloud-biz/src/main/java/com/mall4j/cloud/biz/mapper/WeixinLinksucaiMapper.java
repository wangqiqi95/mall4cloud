package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinLinksucai;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信素材链接表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
public interface WeixinLinksucaiMapper {

	/**
	 * 获取微信素材链接表列表
	 * @return 微信素材链接表列表
	 */
	List<WeixinLinksucai> list();

	/**
	 * 根据微信素材链接表id获取微信素材链接表
	 *
	 * @param id 微信素材链接表id
	 * @return 微信素材链接表
	 */
	WeixinLinksucai getById(@Param("id") String id);

	/**
	 * 保存微信素材链接表
	 * @param weixinLinksucai 微信素材链接表
	 */
	void save(@Param("weixinLinksucai") WeixinLinksucai weixinLinksucai);

	/**
	 * 更新微信素材链接表
	 * @param weixinLinksucai 微信素材链接表
	 */
	void update(@Param("weixinLinksucai") WeixinLinksucai weixinLinksucai);

	/**
	 * 根据微信素材链接表id删除微信素材链接表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
