package com.mall4j.cloud.common.security.adapter;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;

import java.util.List;
import java.util.Set;

/**
 * 实现该接口之后，修改需要授权登陆的路径，不需要授权登陆的路径
 *
 * @author FrozenWatermelon
 * @date 2020/7/4
 */
public interface AuthConfigAdapter {


	/**
	 * 外部直接调用接口，无需登录权限 unwanted auth
	 */
	String EXTERNAL_URI = "/**/ua/**";

	/**
	 * actuator接口 无需登录权限
	 */
	String ACTUATOR_URI = "/actuator/**";

	/**
	 * 也许需要登录才可用的url
	 */
	String MAYBE_AUTH_URI = "/**/ma/**";

	/**
	 * 平台端uri
	 */
	String PLATFORM_URI = "/p/**";

	/**
	 * 商家端uri
	 */
	String MULTISHOP_URI = "/m/**";

	/**
	 * 平台端和商家端都可用的uri
	 */
	String MULTISHOP_PLATFORM_URI = "/mp/**";

	/**
	 * 导购端uri
	 */
	String STAFF_URI = "/s/**";

	/**
	 * 商家开店时可用的uri
	 */
	String MULTSHOP_APPLY_URI = "/**/apply_shop/**";

	/**
	 * feign uri
	 */
	String FEIGN_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/**";

	/**
	 * swagger
	 */
	String DOC_URI = "/v2/api-docs";

	/**
	 * 内部直接调用接口，无需登录权限
	 */
	String FEIGN_INSIDER_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/**";

	/**
	 * 退出登录uri
	 */
	String LOGIN_OUT_URI = "/ma/login_out";
	/**
	 * 需要授权登陆的路径
	 * @return 需要授权登陆的路径列表
	 */
	List<String> pathPatterns();

	/**
	 * 不需要授权登陆的路径
	 * @param paths
	 * @return 不需要授权登陆的路径列表
	 */
	Set<String> excludePathPatterns(String... paths);

}
