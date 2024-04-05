package com.mall4j.cloud.docking.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_erp.config.DockingStdParams;
import com.mall4j.cloud.api.docking.skq_erp.dto.StoreIntegralRankRespDto;
import com.mall4j.cloud.api.docking.skq_wm.config.DockingWmParams;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SpringContextUtils;
import org.apache.commons.collections4.CollectionUtils;
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
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 类描述：调用微盟接口工具类
 *
 * @date 2023/04/26
 */
public class WmClients {

	private static final Logger logger = LoggerFactory.getLogger(WmClients.class);

	/**
	 * 超时时间，60s
	 */
	private static final int DEFAULT_TIMEOUT = 60000;

	private final static String REDIS_WM_ACCESS_TOKEN="mall4cloud_docking:WM_ACCESS_TOKEN_";

	private volatile boolean flag = false;

	private static WmClients clients = new WmClients();

	DockingWmParams dockingWmParams;

	private long lastGetToenTime;

	private String accessToken;

	@Resource
	private RedisTemplate redisTemplate;

	private WmClients() {
	}

	public static WmClients clients() {
		return clients;
	}

	/**
	 * 方法描述：获取网关token
	 *
	 * @return java.lang.String
	 * @date 2022-01-07 18:37:52
	 */
	public String getWmToken() {
		String redisKey = REDIS_WM_ACCESS_TOKEN + getWmParams().getClientId();
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(redisKey);
		String redisAccessToken = boundValueOperations.get();
		if(StringUtils.isNotEmpty(redisAccessToken)){
			return redisAccessToken;
		}
		if (StringUtils.isBlank(accessToken)) {


			long start = System.currentTimeMillis();
			JSONObject body = new JSONObject();
			String post = null;
			try {
				//body.put("code", getWmParams().getCode());
				body.put("grant_type", getWmParams().getGrantType());
				body.put("client_id", getWmParams().getClientId());
				body.put("client_secret", getWmParams().getClientSecret());
				body.put("redirect_uri", getWmParams().getRedirectUri());

				post = HttpUtil.post(getWmParams().getUrl() + "/fuwu/b/oauth2/token?", body);
				if (StringUtils.isNotBlank(post)) {
					JSONObject resp = JSONObject.parseObject(post);
					accessToken = resp.getString("access_token");
					boundValueOperations.set(accessToken);
				}
			} finally {
				logger.info("调用微盟获取access-token接口结束，json参数为:{}, 请求响应为:{}, 共耗时: {}", body, post, System.currentTimeMillis() - start);
			}
		}
		return accessToken;
	}

	/**
	 * 方法描述：调用微盟接口
	 *
	 * @param uri
	 * @param method
	 * @param body
	 * @param respClass
	 * @return T
	 */
	public <T> T postWm(String uri, String method, String body, Class<T> respClass) {
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
			respStr = doPost(url.append(getWmParams().getUrl()).append(uri).append("?").append(HttpUtil.toParams(params)).toString(), body);
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
	 * 方法描述：调用微盟接口
	 *
	 * @param uri
	 * @param method
	 * @param body
	 * @return java.lang.String
	 */
	public String postWm(String uri, String method, String body) {
		long start = System.currentTimeMillis();
		String respStr = null;
		StringBuilder url = new StringBuilder();
		try {
			respStr = doPost(url.append(getWmParams().getUrl()).append(uri).append("?").toString(), body);
		} finally {
			logger.info("调用中台接口结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", url, body, respStr, System.currentTimeMillis() - start);
		}
		return respStr;
	}

	public DockingWmParams getWmParams() {
		if (dockingWmParams == null) {
			dockingWmParams = SpringContextUtils.getBean(DockingWmParams.class);
		}
		return dockingWmParams;
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
			httpPost.addHeader("accesstoken", getWmToken());
			//httpPost.addHeader("Midend-correlation-id", UUID.randomUUID().toString().replace("-", ""));
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
