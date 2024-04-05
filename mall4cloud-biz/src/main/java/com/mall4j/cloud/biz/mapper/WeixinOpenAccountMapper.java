package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinOpenAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信第三方平台账号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 11:05:26
 */
public interface WeixinOpenAccountMapper {

	/**
	 * 获取微信第三方平台账号表列表
	 * @return 微信第三方平台账号表列表
	 */
	List<WeixinOpenAccount> list();

	/**
	 * 根据微信第三方平台账号表id获取微信第三方平台账号表
	 *
	 * @param id 微信第三方平台账号表id
	 * @return 微信第三方平台账号表
	 */
	WeixinOpenAccount getById(@Param("id") Long id);

	/**
	 * 保存微信第三方平台账号表
	 * @param weixinOpenAccount 微信第三方平台账号表
	 */
	void save(@Param("weixinOpenAccount") WeixinOpenAccount weixinOpenAccount);

	/**
	 * 更新微信第三方平台账号表
	 * @param weixinOpenAccount 微信第三方平台账号表
	 */
	void update(@Param("weixinOpenAccount") WeixinOpenAccount weixinOpenAccount);

	/**
	 * 根据微信第三方平台账号表id删除微信第三方平台账号表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 查询，通过appid查询，按照获取ticket时间倒叙
	 * @param appid
	 * @return
	 */
	public WeixinOpenAccount queryOneByAppid(@Param("appid") String appid);
}
