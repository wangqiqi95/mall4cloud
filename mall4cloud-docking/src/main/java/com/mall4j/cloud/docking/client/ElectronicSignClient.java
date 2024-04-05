package com.mall4j.cloud.docking.client;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.mall4j.cloud.common.exception.LuckException;
import org.apache.commons.lang3.StringUtils;

/**
 * 类描述：电子加签请求客户端
 */
public class ElectronicSignClient {


	private static volatile boolean flag = false;

	private IAcsClient acsClient;

	/**
	 * 默认门店id
	 */
	private String defaultStorId;

	public ElectronicSignClient(String regionId, String accessKeyId, String secret) {
		if (!flag) {
			initClient(regionId, accessKeyId, secret);
			flag = true;
		} else {
			throw new LuckException("请勿重复初始化");
		}
	}

	public IAcsClient getClient() {
		return acsClient;
	}

	private void initClient(String regionId, String accessKeyId, String secret) {
		if (StringUtils.isAnyBlank(regionId, accessKeyId, secret)) {
			throw new LuckException("电子加签参数未配置，获取客户端失败");
		}
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
		acsClient = new DefaultAcsClient(profile);
	}

	public String getDefaultStorId() {
		return defaultStorId;
	}

	public void setDefaultStorId(String defaultStorId) {
		this.defaultStorId = defaultStorId;
	}
}
