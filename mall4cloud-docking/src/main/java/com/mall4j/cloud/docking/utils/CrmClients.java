package com.mall4j.cloud.docking.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_crm.config.CrmParamsCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：调用CRM接口工具类
 *
 * @date 2022/1/22 17:15：43
 */
public class CrmClients {

	private static final Logger logger = LoggerFactory.getLogger(CrmClients.class);

	private static CrmClients clients = new CrmClients();

	static CrmParamsCopy crmParamsCopy;

	private CrmClients() {
	}

	public static CrmClients clients() {
		return clients;
	}

	public String get(String uri) {
		return get(uri, null);
	}

	public String get(String uri, Map<String, Object> params) {
		long start = System.currentTimeMillis();
		Map<String, Object> sysParams = createSysParams();
		if (params != null && !params.isEmpty()) {
			sysParams.putAll(params);
		}
		StringBuilder builder = buildUrl(uri);
		String result = null;
		try {
			builder.append("?").append(HttpUtil.toParams(sysParams));
//			result = HttpUtils.doGet(buildUrl(uri).toString(), sysParams);
			result = HttpUtil.get(builder.toString());
		}catch (Exception e){
			logger.error("调用crm接口异常，url为:{}, url参数为:{}, 请求响应为:{}, 共耗时: {}", builder, JSONObject.toJSONString(sysParams), result,
					System.currentTimeMillis() - start);
		} finally {
			logger.info("调用crm接口结束，url为:{}, url参数为:{}, 请求响应为:{}, 共耗时: {}", builder, JSONObject.toJSONString(sysParams), result,
						System.currentTimeMillis() - start);
		}
		return result;
	}

	public String postCrm(String uri, String bodyStr) {
		long start = System.currentTimeMillis();
		StringBuilder builder = buildUrl(uri);
		Map<String, Object> sysParams = createSysParams();
		String result = null;
		try {
			builder.append("?").append(HttpUtil.toParams(sysParams));
			String apiUrl = builder.toString();
			result = HttpUtil.post(apiUrl,bodyStr,6000);
//			result = HttpUtils.doPost(builder.toString(), bodyStr);
		} catch (Exception e){
			e.printStackTrace();
			logger.error("调用crm接口异常，url为:{}, url参数为:{}, json参数为:{}, 异常信息:{}, 共耗时: {}",builder, JSONObject.toJSONString(sysParams), bodyStr, e,
					System.currentTimeMillis() - start);
		}finally {
			logger.info("调用crm接口结束，url为:{}, url参数为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", builder, JSONObject.toJSONString(sysParams), bodyStr, result,
						System.currentTimeMillis() - start);
		}
		return result;
	}

	private StringBuilder buildUrl(String uri) {
		return new StringBuilder(crmParamsCopy.getBaseUrl()).append(uri);
	}

	public String getUri(String uri){
		StringBuilder builder = buildUrl(uri);
		Map<String, Object> sysParams = createSysParams();
		String result = null;
		builder.append("?").append(HttpUtil.toParams(sysParams));
		return builder.toString();
	}

	/**
	 * 生成签名算法
	 *
	 * @param timestamp 当前时间的时间戳（毫秒）
	 * @return 加密签名
	 * @throwsUnsupportedEncodingException
	 */
	public String createSign(long timestamp) {
		try {
			String str = timestamp + "&" + crmParamsCopy.getAppsecret();
			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
			String strMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
			return strMd5.toLowerCase();
		} catch (UnsupportedEncodingException e) {
			logger.error("生成签名异常，timestamp=" + timestamp, e);
		}
		return "";
	}

	/**
	 * 方法描述：生成系统参数查询字符串
	 */
	public Map<String, Object> createSysParams() {
		Map<String, Object> sysParam = new HashMap<>(3);
		sysParam.put("appkey", crmParamsCopy.getAppkey());
		long timeMills = getTimeMills();
		sysParam.put("timestamp", timeMills + "");
		sysParam.put("sign", createSign(timeMills));
		return sysParam;
	}

	/**
	 * 获取精确到秒的时间戳
	 *
	 * @return
	 */
	private static long getTimeMills() {
		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();
		return LocalDateTime
				.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), localTime.getHour(), localTime.getMinute(), localTime.getSecond())
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static void setContext(ApplicationContext applicationContext) {
		crmParamsCopy = applicationContext.getBean(CrmParamsCopy.class);
	}
}
