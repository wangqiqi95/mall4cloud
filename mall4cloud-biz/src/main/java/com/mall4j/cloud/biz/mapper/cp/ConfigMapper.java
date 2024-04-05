package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.Config;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface ConfigMapper {

	/**
	 * 获取企业微信配置表列表
	 * @return 企业微信配置表列表
	 */
	List<Config> list();

	/**
	 * 根据企业微信配置表id获取企业微信配置表
	 *
	 * @param cpId 企业微信配置表id
	 * @return 企业微信配置表
	 */
	Config getByCpId(@Param("cpId") String cpId);

	/**
	 * 保存企业微信配置表
	 * @param config 企业微信配置表
	 */
	void save(@Param("cpConfig") Config config);

	/**
	 * 更新企业微信配置表
	 * @param config 企业微信配置表
	 */
	void update(@Param("cpConfig") Config config);

	/**
	 * 根据企业微信配置表id删除企业微信配置表
	 * @param cpId
	 */
	void deleteById(@Param("cpId") Long cpId);

	/**
	 * 删除所有
	 */
	void deleteAll();
}
