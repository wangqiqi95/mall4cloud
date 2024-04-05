package com.mall4j.cloud.auth.mapper;

import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.common.security.bo.AuthAccountInVerifyBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public interface AuthAccountMapper {

	/**
	 * 根据输入的用户名及用户名类型获取用户信息
	 *
	 * @param inputUserNameType 输入的用户名类型 1.username 2.mobile 3.email
	 * @param inputUserName     输入的用户名
	 * @param sysType           系统类型
	 * @return 用户在token中信息 + 数据库中的密码
	 */
	AuthAccountInVerifyBO getAuthAccountInVerifyByInputUserName(@Param("inputUserNameType") Integer inputUserNameType,
																@Param("inputUserName") String inputUserName, @Param("sysType") Integer sysType);

	/**
	 * 根据用户id 和系统类型获取平台唯一用户
	 *
	 * @param userId  用户id
	 * @param sysType 系统类型
	 * @return 平台唯一用户
	 */
    AuthAccount getByUserIdAndType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

	AuthAccount getBySysTypeAndIsAdminAndTenantId(@Param("sysType") Integer sysType, @Param("isAdmin") Integer isAdmin, @Param("tenantId") Long tenantId);

	/**
	 * 根据getByUid获取平台唯一用户
	 *
	 * @param uid uid
	 * @return 平台唯一用户
	 */
	AuthAccount getByUid(@Param("uid") Long uid);

	/**
	 * 更新密码 根据用户id 和系统类型
	 *
	 * @param userId      用户id
	 * @param sysType     系统类型
	 * @param newPassWord 新密码
	 */
	void updatePassword(@Param("userId") Long userId, @Param("sysType") Integer sysType, @Param("newPassWord") String newPassWord);

	/**
	 * 保存
	 *
	 * @param authAccount
	 */
	void save(@Param("authAccount") AuthAccount authAccount);

	/**
	 * 更新
	 *
	 * @param authAccount authAccount
	 */
    void updateAccountInfo(@Param("authAccount") AuthAccount authAccount);

	/**
	 * 根据用户id和系统类型删除用户
	 *
	 * @param userId  用户id
	 * @param sysType 系统类型
	 */
    void deleteByUserIdAndSysType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

	/**
	 * 根据手机号获取存在用户的数量
	 *
	 * @param mobile  手机号
	 * @param sysType 系统类型
	 * @return 数量
	 */
    int countByMobile(@Param("mobile") String mobile, @Param("sysType") Integer sysType);

	/**
	 * 根据手机号获取uid
	 *
	 * @param validAccount 手机号 or 邮箱
	 * @param systemType   系统类型
	 * @return uid
	 */
	AuthAccount getAccountByInputUserName(@Param("validAccount") String validAccount, @Param("systemType") Integer systemType);

	/**
	 * 根据账号信息，获取对应的用户列表
	 *
	 * @param userName
	 * @param phone
	 * @param email
	 * @param sysType
	 * @return
	 */
	List<AuthAccountVO> listByUserAccount(@Param("userName") String userName, @Param("phone") String phone, @Param("email") String email, @Param("sysType") Integer sysType);

	/**
	 * 根据用户id及系统类型修改用户手机号
	 *
	 * @param authAccountDTO 用户信息
	 * @return void
	 */
	void updateUserMobile(@Param("authAccountDTO") AuthAccountDTO authAccountDTO);

	/**
	 * 根据用户id和系统类型，获取账号列表信息
	 *
	 * @param userIds
	 * @param systemType
	 * @return
	 */
    List<AuthAccountVO> listUserByUserIdsAndType(@Param("userIds") List<Long> userIds, @Param("systemType") Integer systemType);

	/**
	 * 根据用户ids，或者电话号码，系统类型获取用户列表
	 *
	 * @param userIds
	 * @param phone
	 * @param sysType
	 * @return
	 */
	List<AuthAccountVO> listByUserIdsAndPhoneAndType(@Param("userIds") List<Long> userIds, @Param("phone") String phone, @Param("sysType") Integer sysType);

	/**
	 * 根据用户id更新租户id
	 *
	 * @param authAccount
	 * @param userId
	 * @param sysType
	 * @return
	 */
    int updateUserInfoByUserId(@Param("authAccount") AuthAccount authAccount, @Param("userId") Long userId, @Param("sysType") Integer sysType);

	/**
	 * 根据用户id获取用户的微信小程序的userid集合
	 *
	 * @param userId 手机号
	 * @return 第三方微信小程序的userid集合
	 */
	List<String> getBizUserIdListByUserId(@Param("userId") Long userId);

	/**
	 * 根据查询参数获取账户列表
	 *
	 * @param inputUserNameType 输入的用户名类型枚举 1.username 2.mobile 3.email
	 * @param params            用户手机号集合 / 邮箱集合 / 用户名集合
	 * @param sysType           用户类型
	 * @return 账户集合
	 */
    List<AuthAccountVO> getAuthAccountByInputUserName(@Param("inputUserNameType") Integer inputUserNameType, @Param("params") List<String> params, @Param("sysType") Integer sysType);

	/**
	 * 批量注册账户，必须使用条件插入，保证用户批量注册唯一性
	 * 该接口只能于用户批量注册普通用户
	 * 需要确定需要批量插入的集合本身 里面的手机号 邮箱 用户名 不存在重复的数据
	 *
	 * @param authAccounts 账户参数集合
	 * @return 行数
	 */
	int batchSaveAccounts(@Param("authAccounts") List<AuthAccount> authAccounts);

	/**
	 * 统计用户数量
	 *
	 * @param userIds 用户id集合
	 * @param sysType 系统类型
	 * @return 数量
	 */
    int countByUserIds(@Param("userIds") List<Long> userIds, @Param("sysType") Integer sysType);

	/**
	 * 根据租户id获取商家信息
	 *
	 * @param tenantId
	 * @return
	 */
	AuthAccountVO getMerchantInfoByTenantId(@Param("tenantId") Long tenantId);

	/**
	 * 统计手机号码的使用数量
	 *
	 * @param sysType 0.普通用户系统 1.商家端 2平台端
	 * @param mobile  手机号码
	 * @return 数量
	 */
	int countByMobile(@Param("sysType") Integer sysType, @Param("mobile") String mobile);

	/**
	 * 根据用户名和用户类型查看用户名的使用数量
	 *
	 * @param userName
	 * @param sysType
	 * @return
	 */
    int countByUserNameAndSysType(@Param("userName") String userName, @Param("sysType") Integer sysType);

	/**
	 * 获取已存在的用户名称
	 * @param names
	 * @return
	 */
    List<String> listUserNameByNames(@Param("names") List<String> names);
}
