package com.mall4j.cloud.docking.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.jos.dto.JosCommonReqParam;
import com.mall4j.cloud.api.docking.jos.dto.JosIntefaceContext;
import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: jos接口调用工具类
 * @date 2021/12/23 23:28
 */
public class JosClients {
	private static final Logger logger = LoggerFactory.getLogger(JosClients.class);

	/**
	 * 超时时间，60s
	 */
	private static final int DEFAULT_TIMEOUT = 60000;

	private String josUrl;
	private String appKey;
	private String appSecret;

	private JosIntefaceContext context;

	private volatile boolean flag = false;

	private static JosClients clients = new JosClients();

	private JosClients() {
	}

	public static JosClients clients() {
		return clients;
	}

	public void init(String url, String appKey, String appSecret, JosIntefaceContext context) {
		if (!flag) {
			this.josUrl = url;
			this.appKey = appKey;
			this.appSecret = appSecret;
			this.context = context;
		} else {
			throw new LuckException("请勿重复初始化jos客户端");
		}
	}

	/**
	 * 调用 益世接口
	 * @param url			请求地址
	 * @param method		接口名称
	 * @param josParam		请求参数
	 * @return		响应结果
	 */
	public String req(String url, String method, IJosParam josParam) {
		return req(url, method, josParam, DEFAULT_TIMEOUT);
	}

	/**
	 * 调用 益世接口
	 * @param url			请求地址
	 * @param method		接口名称
	 * @param josParam		请求参数
	 * @param timeout		超时时间
	 * @return		响应结果
	 */
	public String req(String url, String method, IJosParam josParam, int timeout) {
		if (StringUtils.isAnyBlank(appKey, appSecret)) {
			throw new LuckException("jos客户端尚未初始化");
		}
		String defaultResult = "{ \"code\": 0,\"msg\": \"调用失败\"}";
		long start = System.currentTimeMillis();
		String result = "";
		JosCommonReqParam<IJosParam> reqParam = getiJosParamJosCommonReqParam(method, josParam);
		String param = reqParam.convertUrlString();
		StringBuilder urlString = new StringBuilder(this.josUrl).append("?").append(param);
		try {
//			result = HttpUtils.doGet(urlString.toString());
			result = HttpUtil.get(urlString.toString());
		} finally {
			logger.info("调用益世接口url:{},method:{},请求参数为:{},请求响应为:{},共耗时:{}", urlString, method, param, result, System.currentTimeMillis() - start);
		}
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObject = JSON.parseObject(result);
			JSONObject jingdong_ysdk_memberApplyJsfService_saveMemberAndProtocolInfo_responce = jsonObject
					.getJSONObject(method.replace(".", "_") + "_responce");
			JSONObject error_response = jsonObject.getJSONObject("error_response");
			if (jingdong_ysdk_memberApplyJsfService_saveMemberAndProtocolInfo_responce != null) {
				JSONObject returnType = jingdong_ysdk_memberApplyJsfService_saveMemberAndProtocolInfo_responce.getJSONObject("returnType");
				if (returnType == null) {
					returnType = jingdong_ysdk_memberApplyJsfService_saveMemberAndProtocolInfo_responce.getJSONObject("response");
				}
				if (returnType.containsKey("data")) {
					returnType.put("data", returnType.getJSONObject("data"));
				}
				return returnType.toJSONString();
			}
			if (error_response != null) {
				JSONObject error = new JSONObject();
				error.put("code", error_response.getString("code"));
				error.put("msg", error_response.getString("zh_desc"));
				return error.toJSONString();
			}
		}
		return defaultResult;
	}

	public String timestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		String gmt = dateFormat.format(new Date()) + "+0800";
		return gmt;
	}

	private JosCommonReqParam<IJosParam> getiJosParamJosCommonReqParam(String method, IJosParam josParam) {
		JosCommonReqParam<IJosParam> reqParam = new JosCommonReqParam<>();
		reqParam.setMemberAndProtocolInfoJson(josParam);
		reqParam.setMethod(method);
		reqParam.setApp_key(appKey);
		reqParam.setTimestamp(DateUtils.dateToString(new Date()));
		reqParam.getMemberAndProtocolInfoJson().setJosContext(this.context);
		reqParam.setSign(buildSign(reqParam.buildSign(), appSecret));
		return reqParam;
	}

	/**
	 * 生成签名
	 * @param signStr
	 * @return
	 */
	public String buildSign(String signStr, String appSecret) {
		if (StringUtils.isNotBlank(signStr)) {
			StringBuilder sign = new StringBuilder();
			sign.append(appSecret);
			sign.append(signStr);
			sign.append(appSecret);
			return MD5Util.encode(sign.toString()).toUpperCase();
		}
		return null;
 	}

}
