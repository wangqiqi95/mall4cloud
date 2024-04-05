package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinOpenAccount;

/**
 * 微信第三方平台账号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 11:05:26
 */
public interface WeixinOpenAccountService {

	/**
	 * 分页获取微信第三方平台账号表列表
	 * @param pageDTO 分页参数
	 * @return 微信第三方平台账号表列表分页数据
	 */
	PageVO<WeixinOpenAccount> page(PageDTO pageDTO);

	/**
	 * 根据微信第三方平台账号表id获取微信第三方平台账号表
	 *
	 * @param id 微信第三方平台账号表id
	 * @return 微信第三方平台账号表
	 */
	WeixinOpenAccount getById(Long id);

	/**
	 * 保存微信第三方平台账号表
	 * @param weixinOpenAccount 微信第三方平台账号表
	 */
	void save(WeixinOpenAccount weixinOpenAccount);

	/**
	 * 更新微信第三方平台账号表
	 * @param weixinOpenAccount 微信第三方平台账号表
	 */
	void update(WeixinOpenAccount weixinOpenAccount);

	/**
	 * 根据微信第三方平台账号表id删除微信第三方平台账号表
	 * @param id 微信第三方平台账号表id
	 */
	void deleteById(Long id);

	/**
	 * 查询，通过appid查询，按照获取ticket时间倒叙
	 * @param appid
	 * @return
	 */
	public WeixinOpenAccount queryOneByAppid(String appid);
}
