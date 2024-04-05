package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信配置信息表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 15:58:07
 */
public interface WeixinConfigMapper {

	/**
	 * 获取微信配置信息表列表
	 * @return 微信配置信息表列表
	 */
	List<WeixinConfig> list();

	/**
	 * 根据微信配置信息表id获取微信配置信息表
	 *
	 * @param id 微信配置信息表id
	 * @return 微信配置信息表
	 */
	WeixinConfig getById(@Param("id") Long id);

	/**
	 * 保存微信配置信息表
	 * @param weixinConfig 微信配置信息表
	 */
	void save(@Param("weixinConfig") WeixinConfig weixinConfig);

	/**
	 * 更新微信配置信息表
	 * @param weixinConfig 微信配置信息表
	 */
	void update(@Param("weixinConfig") WeixinConfig weixinConfig);

	/**
	 * 根据微信配置信息表id删除微信配置信息表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	WeixinConfig getWeixinConfigByKey(@Param("appid") String appid,@Param("paramKey") String paramKey);
}
