package com.mall4j.cloud.auth.mapper;

import com.mall4j.cloud.auth.model.AuthSocial;
import org.apache.ibatis.annotations.Param;

/**
 * 用户社交登陆信息
 *
 * @author lhd
 * @date 2021-01-06 10:26:53
 */
public interface AuthSocialMapper {

	/**
	 * 保存用户社交登陆信息
	 *
	 * @param authSocial 用户社交登陆信息
	 */
	void save(@Param("authSocial") AuthSocial authSocial);

	/**
	 * 更新用户社交登陆信息
	 *
	 * @param authSocial 用户社交登陆信息
	 */
	void update(@Param("authSocial") AuthSocial authSocial);

	/**
	 * 根据用户社交登陆信息id删除用户社交登陆信息
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据社交账号userId和类型获取用户社交账号信息
	 *
	 * @param bizUserId  第三方系统userId
	 * @param socialType 第三方系统类型
	 * @return 用户社交账号信息
	 */
	AuthSocial getByBizUserIdAndType(@Param("bizUserId") String bizUserId, @Param("socialType") Integer socialType);

	AuthSocial getByBizUnionIdAndType(@Param("bizUnionId") String bizUserId, @Param("socialType") Integer socialType);

	/**
	 * 获取根据尝试社交登录时，保存的临时的uid获取社交
	 *
	 * @param tempUid tempUid
	 * @return 用户社交账号信息
	 */
    AuthSocial getByTempUid(@Param("tempUid") String tempUid);

	AuthSocial getByUid(@Param("uid") Long uid);

	/**
	 * 绑定社交账号，通过tempuid
	 *
	 * @param uid     uid
	 * @param tempUid tempUid
	 */
	void bindUidByTempUid(@Param("uid") Long uid, @Param("tempUid") String tempUid);

	void bindUidById(@Param("uid") Long uid, @Param("id") Long id);

	/**
	 * 获取在微信公众号/小程序 绑定的账号数量
	 *
	 * @param uid        uid
	 * @param socialType 社交帐号类型
	 * @return 绑定账号的数量
	 */
    int countByUidAndSocialType(@Param("uid") Long uid, @Param("socialType") Integer socialType);

	AuthSocial getById(@Param("id") Long id);

	int updateUnionIdByUid(@Param("uid") Long uid, @Param("unionId") String unionId);
}
