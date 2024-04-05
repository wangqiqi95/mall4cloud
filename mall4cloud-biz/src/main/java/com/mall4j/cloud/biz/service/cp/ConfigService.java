package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.Config;


/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface ConfigService {


	/**
	 * 根据企业微信配置表id获取企业微信配置表
	 *
	 * @param cpId 企业微信配置表id
	 * @return 企业微信配置表
	 */
	Config getByCpId(String cpId);

	/**
	 * 保存企业微信配置表
	 * @param config 企业微信配置表
	 */
	void save(Config config);

	/**
	 * 更新企业微信配置表
	 * @param config 企业微信配置表
	 */
	void update(Config config);

	/**
	 * 根据企业微信配置表id删除企业微信配置表
	 * @param cpId 企业微信配置表id
	 */
	void deleteById(Long cpId);

	/**
	 * 获取单个企业微信的注册信息
	 * @return Config
	 */
	Config getConfig();
}
