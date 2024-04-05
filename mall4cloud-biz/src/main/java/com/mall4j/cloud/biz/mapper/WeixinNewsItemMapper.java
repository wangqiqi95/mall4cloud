package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinNewsItem;
import com.mall4j.cloud.biz.vo.WeixinNewsItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信图文模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
public interface WeixinNewsItemMapper {

	/**
	 * 获取微信图文模板素材表列表
	 * @return 微信图文模板素材表列表
	 */
	List<WeixinNewsItem> list();

	/**
	 * 根据微信图文模板素材表id获取微信图文模板素材表
	 *
	 * @param id 微信图文模板素材表id
	 * @return 微信图文模板素材表
	 */
	WeixinNewsItem getById(@Param("id") Long id);

	/**
	 * 保存微信图文模板素材表
	 * @param weixinNewsItem 微信图文模板素材表
	 */
	void save(@Param("weixinNewsItem") WeixinNewsItem weixinNewsItem);

	/**
	 * 更新微信图文模板素材表
	 * @param weixinNewsItem 微信图文模板素材表
	 */
	void update(@Param("weixinNewsItem") WeixinNewsItem weixinNewsItem);

	/**
	 * 根据微信图文模板素材表id删除微信图文模板素材表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<WeixinNewsItemVO> getWeixinNewsItemList(@Param("newstemplateId") String newstemplateId);
}
