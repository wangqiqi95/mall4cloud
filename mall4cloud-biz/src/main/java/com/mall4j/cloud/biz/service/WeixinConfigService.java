package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 微信配置信息表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 15:58:07
 */
public interface WeixinConfigService {

	/**
	 * 分页获取微信配置信息表列表
	 * @param pageDTO 分页参数
	 * @return 微信配置信息表列表分页数据
	 */
	PageVO<WeixinConfig> page(PageDTO pageDTO);

	/**
	 * 根据微信配置信息表id获取微信配置信息表
	 *
	 * @param id 微信配置信息表id
	 * @return 微信配置信息表
	 */
	WeixinConfig getById(Long id);

	/**
	 * 保存微信配置信息表
	 * @param weixinConfig 微信配置信息表
	 */
	void save(WeixinConfig weixinConfig);

	/**
	 * 更新微信配置信息表
	 * @param weixinConfig 微信配置信息表
	 */
	void update(WeixinConfig weixinConfig);

	/**
	 * 根据微信配置信息表id删除微信配置信息表
	 * @param id 微信配置信息表id
	 */
	void deleteById(Long id);

	WeixinConfig getWeixinConfigByKey(String appid, String paramKey);

	void saveOrUpdateWeixinConfig(String appid, String paramKey,String openState);
}
