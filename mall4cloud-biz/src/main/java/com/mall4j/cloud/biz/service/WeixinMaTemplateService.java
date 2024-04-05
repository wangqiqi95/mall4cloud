package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信小程序模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
public interface WeixinMaTemplateService {

	/**
	 * 分页获取微信小程序模板素材表列表
	 * @param pageDTO 分页参数
	 * @return 微信小程序模板素材表列表分页数据
	 */
	PageVO<WeixinMaTemplate> page(PageDTO pageDTO);

	/**
	 * 根据微信小程序模板素材表id获取微信小程序模板素材表
	 *
	 * @param id 微信小程序模板素材表id
	 * @return 微信小程序模板素材表
	 */
	WeixinMaTemplate getById(String id);

	/**
	 * 保存微信小程序模板素材表
	 * @param weixinMaTemplate 微信小程序模板素材表
	 */
	void save(WeixinMaTemplate weixinMaTemplate);

	/**
	 * 更新微信小程序模板素材表
	 * @param weixinMaTemplate 微信小程序模板素材表
	 */
	void update(WeixinMaTemplate weixinMaTemplate);

	void updateMediaId(WeixinMaTemplate weixinMaTemplate);

	/**
	 * 根据微信小程序模板素材表id删除微信小程序模板素材表
	 * @param id 微信小程序模板素材表id
	 */
	void deleteById(String id);

	List<WeixinMaTemplate> listAfterThreeDayPicMediaIds();

}
