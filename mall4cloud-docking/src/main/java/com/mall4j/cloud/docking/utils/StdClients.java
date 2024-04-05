package com.mall4j.cloud.docking.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_erp.config.DockingStdParams;
import com.mall4j.cloud.common.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 类描述：调用中台接口工具类
 *
 * @date 2022/1/7 17:49：33
 */
public class StdClients {

	private static final Logger logger = LoggerFactory.getLogger(StdClients.class);

	/**
	 * 超时时间，60s
	 */
	private static final int DEFAULT_TIMEOUT = 60000;

	private volatile boolean flag = false;

	private static StdClients clients = new StdClients();

	DockingStdParams dockingStdParams;

	StdSignUtils stdSignUtils;

	private long lastGetToenTime;

	private String loginToken;

	private StdClients() {
	}

	public static StdClients clients() {
		return clients;
	}

	/**
	 * 方法描述：获取网关token
	 *
	 * @return java.lang.String
	 * @date 2022-01-07 18:37:52
	 */
	public String getStdToken() {
		if (StringUtils.isBlank(loginToken) || System.currentTimeMillis() - lastGetToenTime > getStdParams().getTokenExpire() * 1000l) {
			long start = System.currentTimeMillis();
			Map<String, String> urlParams = new HashMap<>(2);
			JSONObject body = new JSONObject();
			String post = null;
			try {
				urlParams.put("version", "v1");
				urlParams.put("timestamp", System.currentTimeMillis() + "");

				body.put("userName", getStdParams().getUserName());
				body.put("userKey", MD5Util.getSHA256Str(getStdParams().getUserKey()));
				body.put("requestSign", MD5Util.getSHA256Str(body.getString("userName") + body.getString("userKey") + getStdParams().getSecretkey()));
				post = HttpUtil.post(getStdParams().getUrl() + "/api/auth/loginSha256?" + HttpUtil.toParams(urlParams), body, DEFAULT_TIMEOUT);

				if (StringUtils.isNotBlank(post)) {
					JSONObject resp = JSONObject.parseObject(post);
					Boolean success = resp.getBoolean("success");
					if (success != null && success) {
						lastGetToenTime = System.currentTimeMillis();
						loginToken = resp.getString("loginToken");
					}
				}
			} finally {
				logger.info("调用中台获取网关token接口结束，url参数为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", JSONObject.toJSONString(urlParams), body, post,
							System.currentTimeMillis() - start);
				if (urlParams != null && !urlParams.isEmpty()) {
					urlParams.clear();
					urlParams = null;
				}
			}
		}
		return loginToken;
	}

	/**
	 * 方法描述：调用中台接口
	 *
	 * @param uri
	 * @param method
	 * @param body
	 * @param respClass
	 * @return T
	 * @date 2022-01-07 18:08:51
	 */
	public <T> T postStd(String uri, String method, String body, Class<T> respClass) {
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<>(3);
		String respStr = null;
		T t = null;
		StringBuilder url = new StringBuilder();
		try {
			// 取token
			params.put("method", method);
			params.put("version", "v1");
			params.put("timestamp", System.currentTimeMillis() + "");
			respStr = doPost(url.append(getStdParams().getUrl()).append(uri).append("?").append(HttpUtil.toParams(params)).toString(), body);
			if (StringUtils.isNotBlank(respStr)) {
				t = JSONObject.parseObject(respStr, respClass);
			}
		} finally {
			logger.info("调用中台接口结束，url为:{}, url参数为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", url, JSONObject.toJSONString(params), body, respStr,
						System.currentTimeMillis() - start);
		}
		return t;
	}

	/**
	 * 方法描述：调用中台接口
	 *
	 * @param uri
	 * @param method
	 * @param body
	 * @return java.lang.String
	 * @date 2022-01-07 18:08:51
	 */
	public String
	postStd(String uri, String method, String body) {
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<>(3);
		String respStr = null;
		StringBuilder url = new StringBuilder();
		try {
			// 取token
			params.put("method", method);
			params.put("version", "v1");
			params.put("timestamp", System.currentTimeMillis() + "");
			respStr = doPost(url.append(getStdParams().getUrl()).append(uri).append("?").append(HttpUtil.toParams(params)).toString(), body);
		} finally {
			logger.info("调用中台接口结束，url为:{}, url参数为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", url, JSONObject.toJSONString(params), body, respStr,
						System.currentTimeMillis() - start);
		}
		return respStr;
	}

	public DockingStdParams getStdParams() {
		if (dockingStdParams == null) {
			dockingStdParams = SpringContextUtils.getBean(DockingStdParams.class);
		}
		return dockingStdParams;
	}

	public StdSignUtils getStdSignUtils() {
		if (stdSignUtils == null) {
			stdSignUtils = SpringContextUtils.getBean(StdSignUtils.class);
		}
		return stdSignUtils;
	}

	public String doPost(String apiUrl, String json) {
		CloseableHttpClient httpClient = null;

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(DEFAULT_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(DEFAULT_TIMEOUT);
		// 设置从连接池获取连接实例的超时

		if (apiUrl.startsWith("https")) {
			httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setDefaultRequestConfig(configBuilder.build()).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(configBuilder.build());
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("r3-api-token", getStdToken());
			httpPost.addHeader("Midend-correlation-id", UUID.randomUUID().toString().replace("-", ""));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			logger.error("请求url：" + apiUrl + ",数据：" + json + "异常", e);
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * 创建SSL安全连接
	 *
	 * @return
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return sslsf;
	}
}
