package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信公共回复内容表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:03
 */
public interface WeixinAutoResponseService {

	/**
	 * 分页获取微信公共回复内容表列表
	 * @param pageDTO 分页参数
	 * @return 微信公共回复内容表列表分页数据
	 */
	PageVO<WeixinAutoResponse> page(PageDTO pageDTO);

	/**
	 * 根据微信公共回复内容表id获取微信公共回复内容表
	 *
	 * @param id 微信公共回复内容表id
	 * @return 微信公共回复内容表
	 */
	WeixinAutoResponse getById(Long id);

	/**
	 * 保存微信公共回复内容表
	 * @param weixinAutoResponse 微信公共回复内容表
	 */
	void save(WeixinAutoResponse weixinAutoResponse);

	/**
	 * 更新微信公共回复内容表
	 * @param weixinAutoResponse 微信公共回复内容表
	 */
	void update(WeixinAutoResponse weixinAutoResponse);

	/**
	 * 根据微信公共回复内容表id删除微信公共回复内容表
	 * @param id 微信公共回复内容表id
	 */
	void deleteById(Long id);

	void deleteByDataId(String dataId);

	WeixinAutoResponse getWeixinAuto(String dataId, Integer dataSrc,String templateId);

	List<WeixinAutoResponse> getWeixinAutos(String dataId, Integer dataSrc);

	void saveWeixinAutoResponse(String dataId, Integer dataSrc, WeixinTemplageManagerVO templageManagerVO);

	void saveWeixinAutoResponseList(String dataId, Integer dataSrc, WeixinTemplageManagerVO templageManagerVO);
}
