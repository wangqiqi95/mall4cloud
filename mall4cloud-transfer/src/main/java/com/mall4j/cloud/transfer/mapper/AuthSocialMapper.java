package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.AuthSocial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户社交登陆信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 14:24:20
 */
public interface AuthSocialMapper {

	/**
	 * 获取用户社交登陆信息列表
	 * @return 用户社交登陆信息列表
	 */
	List<AuthSocial> list();

	/**
	 * 根据用户社交登陆信息id获取用户社交登陆信息
	 *
	 * @param id 用户社交登陆信息id
	 * @return 用户社交登陆信息
	 */
	AuthSocial getById(@Param("id") Long id);

	/**
	 * 保存用户社交登陆信息
	 * @param authSocial 用户社交登陆信息
	 */
	void save(@Param("authSocial") AuthSocial authSocial);

	void batchSave(@Param("authSocials") List<AuthSocial> authSocials);

    void batchSave2(@Param("authSocials") List<AuthSocial> authSocials);

    void batchSave3(@Param("authSocials") List<AuthSocial> authSocials);

	/**
	 * 更新用户社交登陆信息
	 * @param authSocial 用户社交登陆信息
	 */
	void update(@Param("authSocial") AuthSocial authSocial);

	/**
	 * 根据用户社交登陆信息id删除用户社交登陆信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
