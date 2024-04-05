package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信图片模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
public interface WeixinImgTemplateService {

	/**
	 * 分页获取微信图片模板表列表
	 * @param pageDTO 分页参数
	 * @return 微信图片模板表列表分页数据
	 */
	PageVO<WeixinImgTemplate> page(PageDTO pageDTO);

	/**
	 * 根据微信图片模板表id获取微信图片模板表
	 *
	 * @param id 微信图片模板表id
	 * @return 微信图片模板表
	 */
	WeixinImgTemplate getById(String id);

	/**
	 * 保存微信图片模板表
	 * @param weixinImgTemplate 微信图片模板表
	 */
	void save(WeixinImgTemplate weixinImgTemplate);

	/**
	 * 更新微信图片模板表
	 * @param weixinImgTemplate 微信图片模板表
	 */
	void update(WeixinImgTemplate weixinImgTemplate);

	void updateMediaId( WeixinImgTemplate weixinImgTemplate);

	/**
	 * 根据微信图片模板表id删除微信图片模板表
	 * @param id 微信图片模板表id
	 */
	void deleteById(String id);

	List<WeixinImgTemplate> listAfterThreeDayPicMediaIds();
}
