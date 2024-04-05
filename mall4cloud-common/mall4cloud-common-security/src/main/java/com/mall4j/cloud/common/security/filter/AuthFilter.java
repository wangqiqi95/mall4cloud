package com.mall4j.cloud.common.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.feign.TokenFeignClient;
import com.mall4j.cloud.api.rbac.constant.HttpMethodEnum;
import com.mall4j.cloud.api.rbac.feign.PermissionFeignClient;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.handler.HttpHandler;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.security.adapter.AuthConfigAdapter;
import com.mall4j.cloud.common.util.IpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 授权过滤，只要实现AuthConfigAdapter接口，添加对应路径即可：
 *
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
@Component
public class AuthFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Autowired
	private AuthConfigAdapter authConfigAdapter;

	@Autowired
	private HttpHandler httpHandler;

	@Autowired
	private TokenFeignClient tokenFeignClient;

	@Autowired
	private PermissionFeignClient permissionFeignClient;

	@Autowired
	private FeignInsideAuthConfig feignInsideAuthConfig;


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String requestUri = req.getRequestURI();

		if (!feignRequestCheck(req)) {
			httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
			return;
		}

		Set<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();

		AntPathMatcher pathMatcher = new AntPathMatcher();
		// 如果匹配不需要授权的路径，就不需要校验是否需要授权
		if (CollectionUtil.isNotEmpty(excludePathPatterns)) {
			for (String excludePathPattern : excludePathPatterns) {
				logger.info("excludePathPattern:{}, requestUri:{}", excludePathPattern, requestUri);
				if (pathMatcher.match(excludePathPattern, requestUri)) {
					chain.doFilter(req, resp);
					return;
				}
			}
		}

		String accessToken = req.getHeader("Authorization");
		// 也许需要登录，不登陆也能用的uri
		// 比如优惠券接口，登录的时候可以判断是否已经领取过
		// 不能登录的时候会看所有的优惠券，等待领取的时候再登录
		boolean mayAuth = pathMatcher.match(AuthConfigAdapter.MAYBE_AUTH_URI, requestUri);


		UserInfoInTokenBO userInfoInToken = null;

		// 如果有token，就要获取token
		if (StrUtil.isNotBlank(accessToken)) {
			ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenVoServerResponseEntity = newCheckUriAndGetToken(req, accessToken, mayAuth, pathMatcher);
			if (!userInfoInTokenVoServerResponseEntity.isSuccess()) {
				httpHandler.printServerResponseToWeb(userInfoInTokenVoServerResponseEntity);
				return ;
			}
			userInfoInToken = userInfoInTokenVoServerResponseEntity.getData();
		}
		else if (!mayAuth) {
			httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
			return;
		}

		try {
			// 保存上下文
			AuthUserContext.set(userInfoInToken);

			chain.doFilter(req, resp);
		}
		finally {
			AuthUserContext.clean();
		}

	}

	/**
	 * 根据不同平台的token判断是否有url的权限
	 * 用户端用户token失败，提示异常，前端清理token。
	 */
	private ServerResponseEntity<UserInfoInTokenBO> newCheckUriAndGetToken(HttpServletRequest req, String accessToken, boolean mayAuth,AntPathMatcher pathMatcher) {

		String requestUri = req.getRequestURI();
		UserInfoInTokenBO userInfoInToken = null;

		// 退出登录的url是统一的
		if (!pathMatcher.match(AuthConfigAdapter.LOGIN_OUT_URI, requestUri)) {
			boolean isFeign = pathMatcher.match(AuthConfigAdapter.FEIGN_URI, requestUri);
			boolean isPlatform = pathMatcher.match(AuthConfigAdapter.PLATFORM_URI, requestUri);
			boolean isMultiShop = pathMatcher.match(AuthConfigAdapter.MULTISHOP_URI, requestUri);
			boolean isPlatformOrMultiShop = pathMatcher.match(AuthConfigAdapter.MULTISHOP_PLATFORM_URI, requestUri);
			boolean isStaff = pathMatcher.match(AuthConfigAdapter.STAFF_URI, requestUri);

			// 校验token，并返回用户信息
			ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenVoServerResponseEntity = tokenFeignClient
					.checkToken(accessToken);

			// 用户端校验token失败提示前端 清理token重新登录
			if (!userInfoInTokenVoServerResponseEntity.isSuccess() && !mayAuth
					&&!isPlatform && !isMultiShop && !isPlatformOrMultiShop && !isFeign && !isStaff) {
				return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
			}
			//其他端沿用之前逻辑，提示未授权
			if (!userInfoInTokenVoServerResponseEntity.isSuccess() && !mayAuth) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}
			userInfoInToken = userInfoInTokenVoServerResponseEntity.getData();
			if(userInfoInToken==null && !mayAuth){
				logger.error("checkUriAndGetToken get userInfoInToken isnull.requestUri -->{}, accessToken -->{}",requestUri, accessToken);
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 平台端的uri问题
			if (isPlatform && !Objects.equals(SysTypeEnum.PLATFORM.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 商家端的uri问题
			if (isMultiShop) {
				if (!Objects.equals(SysTypeEnum.MULTISHOP.value(), userInfoInToken.getSysType())) {
					return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
				}
				if (Objects.equals(userInfoInToken.getIsPassShop(), StatusEnum.DISABLE.value()) && !pathMatcher.match(AuthConfigAdapter.MULTSHOP_APPLY_URI, requestUri)) {
					// 当商家处于申请开店状态并且调用不属于开店接口时
					throw new LuckException("你没有权限访问该页面");
				}
			}
			// 商家或平台的uri
			if (isPlatformOrMultiShop && !Objects.equals(SysTypeEnum.PLATFORM.value(), userInfoInToken.getSysType())
					&& !Objects.equals(SysTypeEnum.MULTISHOP.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 平台和商家还需要校验用户角色权限
			if ( isPlatform || isMultiShop) {
				// 需要用户角色权限，就去根据用户角色权限判断是否
				if (!checkRbac(userInfoInToken, requestUri, req.getMethod())) {
					return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
				}
			}

			// 导购端的uri问题
			if (isStaff && !Objects.equals(SysTypeEnum.STAFF.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 用户端的uri问题
			if (userInfoInToken !=null && !isPlatform && !isMultiShop && !isPlatformOrMultiShop && !isFeign && !isStaff
					&& !Objects.equals(SysTypeEnum.ORDINARY.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
			}
		}
		return ServerResponseEntity.success(userInfoInToken);
	}

	/**
	 * 根据不同平台的token判断是否有url的权限
	 */
	@Deprecated
	private ServerResponseEntity<UserInfoInTokenBO> checkUriAndGetToken(HttpServletRequest req, String accessToken, boolean mayAuth,AntPathMatcher pathMatcher) {
		// 校验token，并返回用户信息
		ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenVoServerResponseEntity = tokenFeignClient
				.checkToken(accessToken);
		if (!userInfoInTokenVoServerResponseEntity.isSuccess() && !mayAuth) {
			return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
		}
		UserInfoInTokenBO userInfoInToken = userInfoInTokenVoServerResponseEntity.getData();
		String requestUri = req.getRequestURI();

		// 退出登录的url是统一的
		if (userInfoInToken!=null && !pathMatcher.match(AuthConfigAdapter.LOGIN_OUT_URI, requestUri)) {
			boolean isFeign = pathMatcher.match(AuthConfigAdapter.FEIGN_URI, requestUri);
			boolean isPlatform = pathMatcher.match(AuthConfigAdapter.PLATFORM_URI, requestUri);
			boolean isMultiShop = pathMatcher.match(AuthConfigAdapter.MULTISHOP_URI, requestUri);
			boolean isPlatformOrMultiShop = pathMatcher.match(AuthConfigAdapter.MULTISHOP_PLATFORM_URI, requestUri);
			boolean isStaff = pathMatcher.match(AuthConfigAdapter.STAFF_URI, requestUri);
			// 平台端的uri问题
			if (isPlatform && !Objects.equals(SysTypeEnum.PLATFORM.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 商家端的uri问题
			if (isMultiShop) {
				if (!Objects.equals(SysTypeEnum.MULTISHOP.value(), userInfoInToken.getSysType())) {
					return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
				}
				if (Objects.equals(userInfoInToken.getIsPassShop(), StatusEnum.DISABLE.value()) && !pathMatcher.match(AuthConfigAdapter.MULTSHOP_APPLY_URI, requestUri)) {
					// 当商家处于申请开店状态并且调用不属于开店接口时
					throw new LuckException("你没有权限访问该页面");
				}
			}
			// 商家或平台的uri
			if (isPlatformOrMultiShop && !Objects.equals(SysTypeEnum.PLATFORM.value(), userInfoInToken.getSysType())
					&& !Objects.equals(SysTypeEnum.MULTISHOP.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 平台和商家还需要校验用户角色权限
			if (isPlatform || isMultiShop) {
				// 需要用户角色权限，就去根据用户角色权限判断是否
				if (!checkRbac(userInfoInToken, requestUri, req.getMethod())) {
					return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
				}
			}

			// 导购端的uri问题
			if (isStaff && !Objects.equals(SysTypeEnum.STAFF.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
			}

			// 用户端的uri问题
			if (!isPlatform && !isMultiShop && !isPlatformOrMultiShop && !isFeign && !isStaff
					&& !Objects.equals(SysTypeEnum.ORDINARY.value(), userInfoInToken.getSysType())) {
				return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
			}
		}
		return ServerResponseEntity.success(userInfoInToken);
	}

	private boolean feignRequestCheck(HttpServletRequest req) {
		// 不是feign请求，不用校验
		if (!req.getRequestURI().startsWith(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX)) {
			return true;
		}
		String feignInsideSecret = req.getHeader(feignInsideAuthConfig.getKey());
		logger.info("feignInsideSecret:{} {}",feignInsideSecret, feignInsideAuthConfig.getSecret());

		// 校验feign 请求携带的key 和 value是否正确
		if (StrUtil.isBlank(feignInsideSecret) || !Objects.equals(feignInsideSecret,feignInsideAuthConfig.getSecret())) {
			return false;
		}
		// ip白名单
		List<String> ips = feignInsideAuthConfig.getIps();
		// 移除无用的空ip
		ips.removeIf(StrUtil::isBlank);
		// 有ip白名单，且ip不在白名单内，校验失败
		if (CollectionUtil.isNotEmpty(ips)
				&& !ips.contains(IpHelper.getIpAddr())) {
			logger.error("ip not in ip White list: {}, ip, {}", ips, IpHelper.getIpAddr());
			return false;
		}
		return true;
	}




	/**
	 * 用户角色权限校验
	 * @param uri uri
	 * @return 是否校验成功
	 */
	public boolean checkRbac(UserInfoInTokenBO userInfoInToken, String uri, String method) {
		ServerResponseEntity<Boolean> booleanServerResponseEntity = permissionFeignClient
				.checkPermission(userInfoInToken.getUserId(), userInfoInToken.getSysType(),uri,userInfoInToken.getIsAdmin(),HttpMethodEnum.valueOf(method.toUpperCase()).value() );

		if (!booleanServerResponseEntity.isSuccess()) {
			return false;
		}

		return booleanServerResponseEntity.getData();
	}

}
