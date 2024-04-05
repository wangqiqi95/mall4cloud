package com.mall4j.cloud.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.mall4j.cloud.api.rbac.feign.PermissionFeignClient;
import com.mall4j.cloud.auth.dto.AuthenticationDTO;
import com.mall4j.cloud.auth.dto.BindSocialDTO;
import com.mall4j.cloud.auth.dto.CaptchaAuthenticationDTO;
import com.mall4j.cloud.auth.manager.TokenStore;
import com.mall4j.cloud.auth.model.AuthSocial;
import com.mall4j.cloud.auth.service.AuthAccountService;
import com.mall4j.cloud.auth.service.AuthSocialService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "登录")
public class LoginController {

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private AuthAccountService authAccountService;

	@Autowired
	private PermissionFeignClient permissionFeignClient;

	@Autowired
	private AuthSocialService authSocialService;

	@PostMapping("/ua/login")
	@ApiOperation(value = "账号密码(用于前端登录)", notes = "通过账号/手机号/用户名密码登录，还要携带用户的类型，也就是用户所在的系统")
	public ServerResponseEntity<TokenInfoVO> login(
			@Valid @RequestBody AuthenticationDTO authenticationDTO) {
		if (!Objects.equals(authenticationDTO.getSysType(), SysTypeEnum.ORDINARY.value())) {
			return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
		}
		return doLogin(authenticationDTO);
	}

	@PostMapping("/ua/temp_social_login")
	@ApiOperation(value = "临时的社交登录", notes = "微信公众号支付需要openid，但用户又不绑定社交账号，所以这个openId是临时的")
	public ServerResponseEntity<TokenInfoVO> tempSocialLogin(
			@Valid @RequestBody AuthenticationDTO authenticationDTO) {
		if (!Objects.equals(authenticationDTO.getSysType(), SysTypeEnum.ORDINARY.value())) {
			return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
		}
		return doLogin(authenticationDTO);
	}


	@PostMapping("/ua/admin_login")
	@ApiOperation(value = "账号密码 + 验证码登录(用于后台登录)", notes = "通过账号/手机号/用户名密码登录，还要携带用户的类型，也就是用户所在的系统")
	public ServerResponseEntity<TokenInfoVO> login(
			@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
		// 登陆后台登录需要再校验一遍验证码
//		CaptchaVO captchaVO = new CaptchaVO();
//		captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
//		ResponseModel response = captchaService.verification(captchaVO);
//		if (!response.isSuccess()) {
//			return ServerResponseEntity.showFailMsg("验证码已失效，请重新获取");
//		}
		return doLogin(captchaAuthenticationDTO);
	}

	/**
	 * 免校验登录 手动登录获取token
	 * @return
	 */
//	@PostMapping("/ua/test/admin_login")
//	@ApiOperation(value = "账号密码 + 验证码登录(用于后台登录)", notes = "通过账号/手机号/用户名密码登录，还要携带用户的类型，也就是用户所在的系统")
//	public ServerResponseEntity<TokenInfoVO> testAdminLogin(
//			@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
//		// 登陆后台登录需要再校验一遍验证码
////		CaptchaVO captchaVO = new CaptchaVO();
////		captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
////		ResponseModel response = captchaService.verification(captchaVO);
////		if (!response.isSuccess()) {
////			return ServerResponseEntity.showFailMsg("验证码已失效，请重新获取");
////		}
//		return doLogin(captchaAuthenticationDTO);
//	}


	private ServerResponseEntity<TokenInfoVO> doLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
		ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenResponse = authAccountService
				.getUserInfoInTokenByInputUserNameAndPassword(authenticationDTO.getPrincipal(),
						authenticationDTO.getCredentials(), authenticationDTO.getSysType());
		if (!userInfoInTokenResponse.isSuccess()) {
			return ServerResponseEntity.transform(userInfoInTokenResponse);
		}

		UserInfoInTokenBO data = userInfoInTokenResponse.getData();

		ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO = new ClearUserPermissionsCacheDTO();
		clearUserPermissionsCacheDTO.setSysType(data.getSysType());
		clearUserPermissionsCacheDTO.setUserId(data.getUserId());
		// 将以前的权限清理了,以免权限有缓存
		ServerResponseEntity<Void> clearResponseEntity = permissionFeignClient.clearUserPermissionsCache(clearUserPermissionsCacheDTO);

		if (!clearResponseEntity.isSuccess()) {
			return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
		}

		if (StrUtil.isNotBlank(authenticationDTO.getTempUid())) {
			AuthSocial authSocial = authSocialService.getByTempUid(authenticationDTO.getTempUid());
			data.setBizUid(authSocial.getBizUnionid());
			data.setBizUserId(authSocial.getBizUserId());
			data.setSessionKey(authSocial.getBizTempSession());
			data.setSocialType(authSocial.getSocialType());
		}

		// 保存token，返回token数据给前端
		return ServerResponseEntity.success(tokenStore.storeAndGetVo(data));
	}



	@PostMapping("/ma/login_out")
	@ApiOperation(value = "退出登陆", notes = "点击退出登陆，清除token，清除菜单缓存")
	public ServerResponseEntity<Void> loginOut(HttpServletRequest request) {
		UserInfoInTokenBO userInfoInToken = AuthUserContext.get();
		if (userInfoInToken == null) {
			return ServerResponseEntity.success();
		}

		ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO = new ClearUserPermissionsCacheDTO();
		clearUserPermissionsCacheDTO.setSysType(userInfoInToken.getSysType());
		clearUserPermissionsCacheDTO.setUserId(userInfoInToken.getUserId());
		// 将以前的权限清理了,以免权限有缓存
		permissionFeignClient.clearUserPermissionsCache(clearUserPermissionsCacheDTO);

		String accessToken = request.getHeader("Authorization");

		// 删除该用户在该系统当前的token
		tokenStore.deleteCurrentToken(userInfoInToken.getSysType().toString(), userInfoInToken.getUid(),accessToken);



		return ServerResponseEntity.success();
	}

	@PutMapping("/checkPhoneIsBind")
	@ApiOperation(value="检查用户修改后的手机号是否已经绑定其他账号", notes="检查用户修改后的手机号是否已经绑定其他账号")
	public ServerResponseEntity<Boolean> checkPhoneIsBind(@RequestBody BindSocialDTO checkSmsDTO) {

		int accountNum = authAccountService.getByMobile(checkSmsDTO.getValidAccount());
		if (accountNum > 0) {
			// 此用户不存在，请先注册
			throw new LuckException("此手机号已经绑定过其他账号，请重新输入。");
		}
		return ServerResponseEntity.success(true);
	}

}
