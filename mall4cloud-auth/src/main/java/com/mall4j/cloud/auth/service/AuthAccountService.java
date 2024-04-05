package com.mall4j.cloud.auth.service;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 统一账户信息
 *
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public interface AuthAccountService {

	/**
	 * 通过输入的用户名密码，校验并获取部分用户信息
	 * @param inputUserName 输入的用户名（用户名/邮箱/手机号）
	 * @param password 密码
	 * @param sysType 系统类型 @see SysTypeEnum
	 * @return 用户在token中信息
	 */
	ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserNameAndPassword(String inputUserName,
			String password, Integer sysType);

	/**
	 * 根据用户id 和系统类型获取平台唯一用户
	 * @param userId 用户id
	 * @param sysType 系统类型
	 * @return 平台唯一用户
	 */
	AuthAccount getByUserIdAndType(Long userId, Integer sysType);

	/**
	 * 更新密码 根据用户id 和系统类型
	 * @param userId 用户id
	 * @param sysType 系统类型
	 * @param newPassWord 新密码
	 */
	void updatePassword(Long userId, Integer sysType, String newPassWord);

	/**
	 * 根据getByUid获取平台唯一用户
	 *
	 * @param uid  uid
	 * @return 平台唯一用户
	 */
	AuthAccount getByUid(Long uid);

	/**
	 * 根据手机号获取uid
	 * @param mobile 手机号
	 * @param systemType 系统类型
	 * @return uid
	 */
	AuthAccount getAccountByInputUserName(String mobile, Integer systemType);

	/**
	 * 根据手机号获取用户数量
	 * @param mobile 手机号
	 * @return 数量
	 */
	int getByMobile(String mobile);

	/**
	 * 批量注册账户，必须使用条件插入，保证用户批量注册唯一性
	 * 该接口只能于用户批量注册普通用户
	 * 需要确定需要批量插入的集合本身 里面的手机号 邮箱 用户名 不存在重复的数据
	 * @param authAccounts 账户参数集合
	 * @return 行数
	 */
    int batchSaveAccounts(List<AuthAccount> authAccounts);

	/**
	 * 统计用户数量
	 * @param userIds 用户id集合
	 * @param sysType 系统类型
	 * @return 数量
	 */
	int countByUserIds(List<Long> userIds, Integer sysType);
}
