package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinLinksucai;

/**
 * 微信素材链接表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
public interface WeixinLinksucaiService {

	/**
	 * 分页获取微信素材链接表列表
	 * @param pageDTO 分页参数
	 * @return 微信素材链接表列表分页数据
	 */
	PageVO<WeixinLinksucai> page(PageDTO pageDTO);

	/**
	 * 根据微信素材链接表id获取微信素材链接表
	 *
	 * @param id 微信素材链接表id
	 * @return 微信素材链接表
	 */
	WeixinLinksucai getById(String id);

	/**
	 * 保存微信素材链接表
	 * @param weixinLinksucai 微信素材链接表
	 */
	void save(WeixinLinksucai weixinLinksucai);

	/**
	 * 更新微信素材链接表
	 * @param weixinLinksucai 微信素材链接表
	 */
	void update(WeixinLinksucai weixinLinksucai);

	/**
	 * 根据微信素材链接表id删除微信素材链接表
	 * @param id 微信素材链接表id
	 */
	void deleteById(Long id);
}
