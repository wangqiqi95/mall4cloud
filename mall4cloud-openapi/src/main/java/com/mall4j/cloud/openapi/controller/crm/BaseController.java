package com.mall4j.cloud.openapi.controller.crm;

import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.openapi.config.CrmParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	CrmParams crmParams;

	protected CrmResponse<Void> verify() {
		String sign = request.getHeader("sign");
		String timestamp = request.getHeader("timestamp");
		String appkey = request.getHeader("appkey");

		if (StringUtils.isBlank(appkey)) {
			return CrmResponse.fail("Header参数：appkey不能为空");
		}
		if (!appkey.equals(crmParams.getAppkey())) {
			return CrmResponse.fail("Header参数：appkey不正确");
		}
		if (StringUtils.isBlank(timestamp)) {
			return CrmResponse.fail("Header参数：timestamp不能为空");
		}
		if (StringUtils.isBlank(sign)) {
			return CrmResponse.fail("Header参数：sign不能为空");
		}
		if (!sign.equals(this.createSign(timestamp))) {
			return CrmResponse.fail("Header参数：sign不正确");
		}
		return CrmResponse.success();
	}

	/**
	 * 生成签名算法
	 *
	 * @param timestamp 当前时间的时间戳（毫秒）
	 * @return 加密签名
	 * @throwsUnsupportedEncodingException
	 */
	public String createSign(String timestamp) {
		try {
			String str = timestamp + "&" + crmParams.getAppsecret();
			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
			String strMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
			return strMd5.toLowerCase();
		} catch (UnsupportedEncodingException e) {
			logger.error("生成签名异常，timestamp=" + timestamp, e);
		}
		return "";
	}

	public static void main(String[] args) {
		long l = System.currentTimeMillis();
		System.out.println(l);

		try {
			String str = l + "&" + "c1ac5711bba7a6f4066ca87365b7738c";
			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
			String strMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
			System.out.println(strMd5);
		} catch (UnsupportedEncodingException e) {
		}

	}
}
