package com.mall4j.cloud.api.auth.feign;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.*;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
@FeignClient(value = "mall4cloud-auth",contextId ="account")
public interface AccountFeignClient {

	/**
	 * 保存统一账户
	 * @param authAccountDTO 账户信息
	 * @return Long uid
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account")
	ServerResponseEntity<Long> save(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 更新统一账户
	 * @param authAccountDTO 账户信息
	 * @return void
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account")
	ServerResponseEntity<Void> update(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 更新账户状态
	 * @param authAccountDTO 账户信息
	 * @return void
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/status")
	ServerResponseEntity<Void> updateAuthAccountStatus(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 根据用户id和系统类型删除用户
	 * @param userId 用户id
	 * @return void
	 */
	@DeleteMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/deleteByUserIdAndSysType")
	ServerResponseEntity<Void> deleteByUserIdAndSysType(@RequestParam("userId")Long userId);

	/**
	 * 根据手机号获取存在用户的数量
	 * @param mobile 手机号
	 * @return 数量
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/countByMobile")
	ServerResponseEntity<Integer> countByMobile(@RequestParam("mobile")String mobile);


	/**
	 * 根据用户id和系统类型获取用户信息
	 * @param userId 用户id
	 * @param sysType 系统类型
	 * @return void
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/getByUserIdAndSysType")
	ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(@RequestParam("userId") Long userId,@RequestParam("sysType") Integer sysType);

	/**
	 * 根据用户id和系统类型获取用户信息
	 * @param userId  用户id
	 * @param sysType 系统类型
	 * @return void
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/getBySysTypeAndIsAdmin")
	ServerResponseEntity<AuthAccountVO> getBySysTypeAndIsAdminAndTenantId(@RequestParam("sysType") Integer sysType, @RequestParam("isAdmin") Integer isAdmin, @RequestParam("tenantId") Long tenantId);

	/**
	 * 保存统一账户
	 * @param authAccountWithSocialDTO 账户信息 和社交账号信息
	 * @return uid
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/accountWithSocial")
	ServerResponseEntity<Long> saveAccountWithSocial(@RequestBody AuthAccountWithSocialDTO authAccountWithSocialDTO);

	/**
	 * 保存用户信息，生成token，返回前端
	 * @param userInfoInTokenBO 账户信息 和社交账号信息
	 * @return uid
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/storeTokenAndGetVo")
	ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(@RequestBody UserInfoInTokenBO userInfoInTokenBO);

	/**
	 * 根据账号信息（账号名、手机号、邮箱），获取对应的用户列表
	 * @param userName
	 * @param phone
	 * @param email
	 * @param sysType
	 * @return 用户账号信息列表
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/listByUserAccount")
    ServerResponseEntity<List<AuthAccountVO>> listByUserAccount(@RequestParam(value = "userName",defaultValue = "") String userName, @RequestParam(value = "phone",defaultValue = "") String phone
			, @RequestParam(value = "email",defaultValue = "") String email, @RequestParam("sysType") SysTypeEnum sysType);

	/**
	 * 根据用户id及系统id修改用户手机号
	 * @param authAccountDTO 用户信息
	 * @return void
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/updateUserMobile")
	ServerResponseEntity<Void> updateUserMobile(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 根据用户id和系统类型，获取账号列表信息
	 * @param userIds
	 * @param systemType
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/listUserByUserIdsAndType")
	ServerResponseEntity<List<AuthAccountVO>> listUserByUserIdsAndType(@RequestParam("userIds") List<Long> userIds, @RequestParam("systemType") Integer systemType);

	/**
	 * 根据用户ids，或者电话号码，系统类型获取用户列表
	 * @param userIds
	 * @param phone
	 * @param sysType
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/listByUserIdsAndPhoneAndType")
	ServerResponseEntity<List<AuthAccountVO>> listByUserIdsAndPhoneAndType(@RequestParam("userIds") List<Long> userIds,
																			 @RequestParam(value = "phone",defaultValue = "") String phone,
																			 @RequestParam("sysType") Integer sysType);


	/**
	 * 根据用户id与用户类型更新用户信息
	 * @param userInfoInTokenBO 新的用户信息
	 * @param userId 用户id
	 * @param sysType 用户类型
	 * @return
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/accout/updateTenantIdByUserIdAndSysType")
	ServerResponseEntity<Void> updateUserInfoByUserIdAndSysType(@RequestBody UserInfoInTokenBO userInfoInTokenBO, @RequestParam("userId") Long userId, @RequestParam("sysType") Integer sysType);

	/**
	 * 根据手机号和用户类型获取存在用户的数量
	 * @param mobile 手机号
	 * @param sysType 用户类型
	 * @return 数量
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/countByMobileAndSysType")
	ServerResponseEntity<Integer> countByMobileAndSysType(@RequestParam("mobile") String mobile, @RequestParam("sysType") Integer sysType);

	/**
	 * 修改用户状态，删除用户登录token信息,使用户下线
	 * @param authAccountDTO 账户信息
	 * @return void
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/updateStatusAndDeleteUserToken")
	ServerResponseEntity<Void> updateAuthAccountStatusAndDeleteUserToken(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 根据用户id获取用户的微信小程序的userid集合
	 * @param userId 手机号
	 * @return 第三方微信小程序的userid集合
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/getBizUserIdListByUserId")
	ServerResponseEntity<List<String>> getBizUserIdListByUserId(@RequestParam("userId")Long userId);

	/**
	 * 批量注册用户信息
	 * @param authAccountUserDTO
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/batchRegister")
	ServerResponseEntity<String> batchRegisterAccount(@RequestBody AuthAccountUserDTO authAccountUserDTO);

	/**
	 * 根据租户id查询商家信息
	 * @param tenantId
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/getMerchantInfoByTenantId")
	ServerResponseEntity<AuthAccountVO> getMerchantInfoByTenantId(@RequestParam("tenantId") Long tenantId);

	/**
	 * 验证手机号/邮箱/用户名是否存在
	 * @param authAccountDTO 账户信息
	 * @return Boolean true验证通过,该账号不存在 false验证不通过，该账号存在
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/verifyAccount")
	ServerResponseEntity<Boolean> verifyAccount(@RequestBody AuthAccountDTO authAccountDTO);

	/**
	 * 根据用户名和用户类型获取存在用户的数量
	 * @param userName 用户名
	 * @param sysType 用户类型
	 * @return 数量
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/countByUserNameAndSysType")
    ServerResponseEntity<Integer> countByUserNameAndSysType(@RequestParam("userName") String userName, @RequestParam("sysType") Integer sysType);

	/**
	 * 获取已存在的用户名称
	 * @param names
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/listUserNameByNames")
	ServerResponseEntity<List<String>> listUserNameByNames(@RequestParam("names") List<String> names);
}
