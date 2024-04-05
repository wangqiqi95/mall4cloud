package com.mall4j.cloud.openapi.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.mall4j.cloud.api.openapi.bo.IPHCodeInfoBo;
import com.mall4j.cloud.api.openapi.bo.IPHInfoInTokenBO;
import com.mall4j.cloud.api.openapi.bo.ITokenInfoBo;
import com.mall4j.cloud.api.openapi.bo.TokenInfoBO;
import com.mall4j.cloud.api.openapi.constant.SysTypeEnum;
import com.mall4j.cloud.api.openapi.vo.TokenInfoVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PrincipalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token管理 1. 登陆返回token 2. 刷新token 3. 清除用户过去token 4. 校验token
 *
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
@Component @RefreshScope public class TokenStore {
	private static final Logger logger = LoggerFactory.getLogger(TokenStore.class);

	@Value("${auth.token.expire}") public Integer defaultExpire;

	@Value("${auth.token.iph.expire:}") public Integer iphExpire;

	/**
	 * 用于aes签名的key，16位
	 */
	@Value("${auth.token.signKey}") public String tokenSignKey;

	private final RedisTemplate<Object, Object> redisTemplate;

	private final RedisSerializer<Object> redisSerializer;

	private final StringRedisTemplate stringRedisTemplate;

	public TokenStore(RedisTemplate<Object, Object> redisTemplate, RedisSerializer<Object> redisSerializer, StringRedisTemplate stringRedisTemplate) {
		this.redisTemplate = redisTemplate;
		this.redisSerializer = redisSerializer;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public String storeCode(IPHCodeInfoBo codeInfoBo) {
		String codeKeyStr = getAccessKey(codeInfoBo.getAppkey());
		String code = IdUtil.simpleUUID();
		redisTemplate.opsForValue()
				.set(codeKeyStr, code + StrUtil.COLON + codeInfoBo.getRedirecturi(), getExpiresIn(codeInfoBo.getSysTypeValue()), TimeUnit.SECONDS);
		return code;
	}

	public String getInfoByCode(String appKey) {
		String codeKeyStr = getAccessKey(appKey);
		Object o = redisTemplate.opsForValue().get(codeKeyStr);
		redisTemplate.delete(codeKeyStr);
		return o == null ? "" : o.toString();
	}

	/**
	 * 将用户的部分信息存储在token中，并返回token信息
	 *
	 * @param iphInfoInTokenBO 用户在token中的信息
	 * @return token信息
	 */
	public TokenInfoBO<IPHInfoInTokenBO> storeAccessToken(IPHInfoInTokenBO iphInfoInTokenBO) {
		TokenInfoBO<IPHInfoInTokenBO> tokenInfoBO = new TokenInfoBO();
		String accessToken = IdUtil.simpleUUID();
		String refreshToken = IdUtil.simpleUUID();

		tokenInfoBO.setUserInfoInToken(iphInfoInTokenBO);
		tokenInfoBO.setExpiresIn(getExpiresIn(iphInfoInTokenBO.getSysTypeValue()));

		String uidToAccessKeyStr = getUidToAccessKey(getApprovalKey(iphInfoInTokenBO));
		String accessKeyStr = getAccessKey(accessToken);
		String refreshToAccessKeyStr = getRefreshToAccessKey(refreshToken);

		// 一个用户会登陆很多次，每次登陆的token都会存在 uid_to_access里面
		// 但是每次保存都会更新这个key的时间，而key里面的token有可能会过期，过期就要移除掉
		List<byte[]> existsAccessTokensBytes = new ArrayList<>();
		// 新的token数据
		existsAccessTokensBytes.add((accessToken + StrUtil.COLON + refreshToken).getBytes(StandardCharsets.UTF_8));

		Long size = redisTemplate.opsForSet().size(uidToAccessKeyStr);
		if (size != null && size != 0) {
			List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size);
			if (tokenInfoBoList != null) {
				for (String accessTokenWithRefreshToken : tokenInfoBoList) {
					String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
					String accessTokenData = accessTokenWithRefreshTokenArr[0];
					if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(getAccessKey(accessTokenData)))) {
						existsAccessTokensBytes.add(accessTokenWithRefreshToken.getBytes(StandardCharsets.UTF_8));
					}
				}
			}
		}

		redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

			long expiresIn = tokenInfoBO.getExpiresIn();

			byte[] uidKey = uidToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
			byte[] refreshKey = refreshToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
			byte[] accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);

			connection.sAdd(uidKey, ArrayUtil.toArray(existsAccessTokensBytes, byte[].class));

			// 通过uid + sysType 保存access_token，当需要禁用用户的时候，可以根据uid + sysType 禁用用户
			connection.expire(uidKey, expiresIn);

			// 通过refresh_token获取用户的access_token从而刷新token
			connection.setEx(refreshKey, expiresIn, accessToken.getBytes(StandardCharsets.UTF_8));

			// 通过access_token保存用户的租户id，用户id，uid
			connection.setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(iphInfoInTokenBO)));

			return null;
		});

		// 返回给前端是加密的token
//		tokenInfoBO.setAccessToken(encryptToken(accessToken, iphInfoInTokenBO.getSysTypeValue()));
//		tokenInfoBO.setRefreshToken(encryptToken(refreshToken, iphInfoInTokenBO.getSysTypeValue()));

		tokenInfoBO.setAccessToken(accessToken);
		tokenInfoBO.setRefreshToken(refreshToken);
		return tokenInfoBO;
	}

	/**
	 * 根据accessToken 获取用户信息
	 *
	 * @param accessToken accessToken
	 * @param needDecrypt 是否需要解密
	 * @return 用户信息
	 */
	public ServerResponseEntity<IPHInfoInTokenBO> getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
		if (StrUtil.isBlank(accessToken)) {
			return ServerResponseEntity.showFailMsg("accessToken is blank");
		}
		String realAccessToken;
		if (needDecrypt) {
			ServerResponseEntity<String> decryptTokenEntity = decryptToken(accessToken);
			if (!decryptTokenEntity.isSuccess()) {
				return ServerResponseEntity.transform(decryptTokenEntity);
			}
			realAccessToken = decryptTokenEntity.getData();
		} else {
			realAccessToken = accessToken;
		}
		IPHInfoInTokenBO userInfoInTokenBO = (IPHInfoInTokenBO) redisTemplate.opsForValue().get(getAccessKey(realAccessToken));

		if (userInfoInTokenBO == null) {
			return ServerResponseEntity.showFailMsg("accessToken 已过期");
		}
		return ServerResponseEntity.success(userInfoInTokenBO);
	}

	/**
	 * 刷新token，并返回新的token
	 *
	 * @param refreshToken
	 * @return
	 */
	public ServerResponseEntity<TokenInfoBO> refreshToken(String refreshToken, boolean needDecrypt) {
		if (StrUtil.isBlank(refreshToken)) {
			return ServerResponseEntity.showFailMsg("refreshToken is blank");
		}
		String realRefreshToken;
		if (needDecrypt) {
			ServerResponseEntity<String> decryptTokenEntity = decryptToken(refreshToken);
			if (!decryptTokenEntity.isSuccess()) {
				return ServerResponseEntity.transform(decryptTokenEntity);
			}
			realRefreshToken = decryptTokenEntity.getData();
		} else {
			realRefreshToken = refreshToken;
		}
		String accessToken = stringRedisTemplate.opsForValue().get(getRefreshToAccessKey(realRefreshToken));

		if (StrUtil.isBlank(accessToken)) {
			return ServerResponseEntity.showFailMsg("refreshToken 已过期");
		}
		ServerResponseEntity<IPHInfoInTokenBO> userInfoByAccessTokenEntity = getUserInfoByAccessToken(accessToken, false);

		if (!userInfoByAccessTokenEntity.isSuccess()) {
			return ServerResponseEntity.showFailMsg("refreshToken 已过期");
		}

		IPHInfoInTokenBO userInfoInTokenBO = userInfoByAccessTokenEntity.getData();
		// 删除旧的refresh_token
		stringRedisTemplate.delete(getRefreshToAccessKey(realRefreshToken));
		// 删除旧的access_token
		stringRedisTemplate.delete(getAccessKey(accessToken));
		// 保存一份新的token
		TokenInfoBO<IPHInfoInTokenBO> tokenInfoBO = storeAccessToken(userInfoInTokenBO);
		return ServerResponseEntity.success(tokenInfoBO);
	}

	/**
	 * 删除全部的token
	 */
	public void deleteAllToken(String appId, Long uid) {
		String uidKey = getUidToAccessKey(getApprovalKey(appId, uid));
		Long size = redisTemplate.opsForSet().size(uidKey);
		if (size == null || size == 0) {
			return;
		}
		List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);

		if (CollUtil.isEmpty(tokenInfoBoList)) {
			return;
		}

		for (String accessTokenWithRefreshToken : tokenInfoBoList) {
			String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
			String accessToken = accessTokenWithRefreshTokenArr[0];
			String refreshToken = accessTokenWithRefreshTokenArr[1];
			redisTemplate.delete(getRefreshToAccessKey(refreshToken));
			redisTemplate.delete(getAccessKey(accessToken));
		}
		redisTemplate.delete(uidKey);

	}

	private ServerResponseEntity<String> decryptToken(String data) {
		AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
		String decryptStr;
		String decryptToken;
		try {
			decryptStr = aes.decryptStr(data);
			decryptToken = decryptStr.substring(0, 32);
			// 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
			long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));
			// 系统类型
			int sysType = Integer.parseInt(decryptStr.substring(45));
			// token的过期时间
			int expiresIn = getExpiresIn(sysType);
			long second = 1000L;
			if (System.currentTimeMillis() - createTokenTime > expiresIn * second) {
				return ServerResponseEntity.showFailMsg("token 格式有误");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponseEntity.showFailMsg("token 格式有误");
		}

		// 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
		if (!PrincipalUtil.isSimpleChar(decryptToken)) {
			return ServerResponseEntity.showFailMsg("token 格式有误");
		}
		return ServerResponseEntity.success(decryptToken);
	}

	private String encryptToken(String accessToken, Integer sysType) {
		AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
		return aes.encryptBase64(accessToken + System.currentTimeMillis() + sysType);
	}

	private int getExpiresIn(int sysType) {
		Integer expiresIn = null;

		if (Objects.equals(sysType, SysTypeEnum.IPH.value())) {
			expiresIn = iphExpire;
		}

		if (expiresIn == null) {
			expiresIn = defaultExpire;
		}
		return expiresIn;
	}

	public TokenInfoVO storeAndGetVo(IPHInfoInTokenBO userInfoInToken) {
		TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInToken);

		TokenInfoVO tokenInfoVO = new TokenInfoVO();
		tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
		tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
		tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
		return tokenInfoVO;
	}

	public void deleteCurrentToken(String appId, Long uid, String accessToken) {

		ServerResponseEntity<String> stringServerResponseEntity = decryptToken(accessToken);

		String decryptToken = stringServerResponseEntity.getData();

		String uidKey = getUidToAccessKey(getApprovalKey(appId, uid));
		Long size = redisTemplate.opsForSet().size(uidKey);
		if (size == null || size == 0) {
			return;
		}
		List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);

		if (CollUtil.isEmpty(tokenInfoBoList)) {
			return;
		}
		String dbAccessToken = null;
		String dbRefreshToken = null;

		List<byte[]> list = new ArrayList<>();
		for (String accessTokenWithRefreshToken : tokenInfoBoList) {
			String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
			dbAccessToken = accessTokenWithRefreshTokenArr[0];
			if (decryptToken.equals(dbAccessToken)) {
				dbRefreshToken = accessTokenWithRefreshTokenArr[1];
				redisTemplate.delete(getRefreshToAccessKey(dbRefreshToken));
				redisTemplate.delete(getAccessKey(dbAccessToken));
				continue;
			}
			list.add(accessTokenWithRefreshToken.getBytes(StandardCharsets.UTF_8));
		}

		if (CollUtil.isNotEmpty(list)) {
			redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

				connection.sAdd(uidKey.getBytes(StandardCharsets.UTF_8), ArrayUtil.toArray(list, byte[].class));
				return null;
			});
		}

	}

	private static String getApprovalKey(ITokenInfoBo tokenInfoBo) {
		return getApprovalKey(tokenInfoBo.getSysType().toString(), tokenInfoBo.getUid());
	}

	private static String getApprovalKey(String appId, Long uid) {
		return uid == null ? appId : appId + StrUtil.COLON + uid;
	}

	public String getUidToAccessKey(String approvalKey) {
		return CacheNames.UID_TO_ACCESS + approvalKey;
	}

	public String getAccessKey(String accessToken) {
		return CacheNames.ACCESS + accessToken;
	}

	public String getRefreshToAccessKey(String refreshToken) {
		return CacheNames.REFRESH_TO_ACCESS + refreshToken;
	}
}
