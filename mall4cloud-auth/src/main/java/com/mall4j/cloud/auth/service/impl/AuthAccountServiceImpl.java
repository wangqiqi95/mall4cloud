package com.mall4j.cloud.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.auth.constant.AuthAccountStatusEnum;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.common.security.bo.AuthAccountInVerifyBO;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.security.constant.InputUserNameEnum;
import com.mall4j.cloud.auth.mapper.AuthAccountMapper;
import com.mall4j.cloud.auth.service.AuthAccountService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PrincipalUtil;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
@Service
public class AuthAccountServiceImpl implements AuthAccountService {

	@Resource
	private AuthAccountMapper authAccountMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MapperFacade mapperFacade;

	public static final String USER_NOT_FOUND_SECRET = "USER_NOT_FOUND_SECRET";

	private static String userNotFoundEncodedPassword;

	@Override
	public ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserNameAndPassword(String inputUserName,
			String password, Integer sysType) {

		if (StrUtil.isBlank(inputUserName)) {
			return ServerResponseEntity.showFailMsg("用户名不能为空");
		}
		if (StrUtil.isBlank(password)) {
			return ServerResponseEntity.showFailMsg("密码不能为空");
		}

		InputUserNameEnum inputUserNameEnum = null;

		// 邮箱
		if (PrincipalUtil.isEmail(inputUserName)) {
			inputUserNameEnum = InputUserNameEnum.EMAIL;
		}

		// 手机号
		if (PrincipalUtil.isMobile(inputUserName)) {
			inputUserNameEnum = InputUserNameEnum.PHONE;
		}

		// 用户名
		if (PrincipalUtil.isUserName(inputUserName)) {
			inputUserNameEnum = InputUserNameEnum.USERNAME;
		}

		if (inputUserNameEnum == null) {
			return ServerResponseEntity.showFailMsg("用户名或密码不正确");
		}

		AuthAccountInVerifyBO authAccountInVerifyBO = authAccountMapper
				.getAuthAccountInVerifyByInputUserName(inputUserNameEnum.value(), inputUserName, sysType);



		if (authAccountInVerifyBO == null) {
			prepareTimingAttackProtection();
			// 再次进行运算，防止计时攻击
			// 计时攻击（Timing
			// attack），通过设备运算的用时来推断出所使用的运算操作，或者通过对比运算的时间推定数据位于哪个存储设备，或者利用通信的时间差进行数据窃取。
			mitigateAgainstTimingAttack(password);
			return ServerResponseEntity.showFailMsg("用户名或密码不正确");
		}

		if (Objects.equals(authAccountInVerifyBO.getStatus(), AuthAccountStatusEnum.DISABLE.value())) {
			return ServerResponseEntity.showFailMsg("用户已禁用，请联系客服");
		}

		if (!passwordEncoder.matches(password, authAccountInVerifyBO.getPassword())) {
			return ServerResponseEntity.showFailMsg("用户名或密码不正确");
		}

		return ServerResponseEntity.success(mapperFacade.map(authAccountInVerifyBO, UserInfoInTokenBO.class));
	}

    @Override
    public AuthAccount getByUserIdAndType(Long userId, Integer sysType) {
        return authAccountMapper.getByUserIdAndType(userId, sysType);
    }

	@Override
	public void updatePassword(Long userId, Integer sysType, String newPassWord) {
		authAccountMapper.updatePassword(userId, sysType, passwordEncoder.encode(newPassWord));
	}

	@Override
	public AuthAccount getByUid(Long uid) {
		return authAccountMapper.getByUid(uid);
	}

    @Override
    public AuthAccount getAccountByInputUserName(String mobile, Integer systemType) {
        return authAccountMapper.getAccountByInputUserName(mobile,systemType);
    }

    @Override
    public int getByMobile(String mobile) {

		return authAccountMapper.countByMobile(mobile, SysTypeEnum.ORDINARY.value());
    }

    @Override
    public int batchSaveAccounts(List<AuthAccount> authAccounts) {
		return authAccountMapper.batchSaveAccounts(authAccounts);
    }

    @Override
    public int countByUserIds(List<Long> userIds, Integer sysType) {
		return authAccountMapper.countByUserIds(userIds,sysType);
    }

    /**
	 * 防止计时攻击
	 */
	private void prepareTimingAttackProtection() {
		if (userNotFoundEncodedPassword == null) {
			userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_SECRET);
		}
	}

	/**
	 * 防止计时攻击
	 */
	private void mitigateAgainstTimingAttack(String presentedPassword) {
		if (presentedPassword != null) {
			this.passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
		}
	}

}
