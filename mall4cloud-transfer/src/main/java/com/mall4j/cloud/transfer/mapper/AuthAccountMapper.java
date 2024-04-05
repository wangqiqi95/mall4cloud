package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.AuthAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统一账户信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 14:24:19
 */
public interface AuthAccountMapper {

	/**
	 * 获取统一账户信息列表
	 * @return 统一账户信息列表
	 */
	List<AuthAccount> list();

	/**
	 * 根据统一账户信息id获取统一账户信息
	 *
	 * @param uid 统一账户信息id
	 * @return 统一账户信息
	 */
	AuthAccount getByUid(@Param("uid") Long uid);

	/**
	 * 保存统一账户信息
	 * @param authAccount 统一账户信息
	 */
	void save(@Param("authAccount") AuthAccount authAccount);

	void batchSave(@Param("authAccounts") List<AuthAccount> authAccounts);

    void batchSave2(@Param("authAccounts") List<AuthAccount> authAccounts);

    void batchSave3(@Param("authAccounts") List<AuthAccount> authAccounts);

	/**
	 * 更新统一账户信息
	 * @param authAccount 统一账户信息
	 */
	void update(@Param("authAccount") AuthAccount authAccount);

	/**
	 * 根据统一账户信息id删除统一账户信息
	 * @param uid
	 */
	void deleteById(@Param("uid") Long uid);
}
