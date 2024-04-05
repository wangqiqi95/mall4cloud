package com.mall4j.cloud.api.docking.jos.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: jos 公共请求参数
 * @date 2021/12/23 22:10
 */
public class JosCommonReqParam<T extends IJosParam> implements Serializable {

	private static final long serialVersionUID = 7006751711604197506L;

	@ApiModelProperty(value = "API接口名称", required = true)
	private String method;

	@ApiModelProperty(value = "采用OAuth授权方式为必填参数")
	private String access_token;

	@ApiModelProperty(value = "应用的app_key", required = true)
	private String app_key;

	@ApiModelProperty(value = "签名", required = true)
	private String sign;

	@ApiModelProperty(value = "时间戳，格式为yyyy-MM-dd HH:mm:ss，例如：2011-06-16 13:23:30。京东API服务端允许客户端请求时间误差为10分钟", required = true)
	private String timestamp;

	@ApiModelProperty(value = "暂时只支持json,默认值即可")
	private String format = "json";

	@ApiModelProperty(value = "版本：2.0,默认值即可")
	private String v = "2.0";

	private T memberAndProtocolInfoJson;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public T getMemberAndProtocolInfoJson() {
		return memberAndProtocolInfoJson;
	}

	public void setMemberAndProtocolInfoJson(T memberAndProtocolInfoJson) {
		this.memberAndProtocolInfoJson = memberAndProtocolInfoJson;
	}

	@Override
	public String toString() {
		return "JosCommonReqParam{" + "method='" + method + '\'' + ", access_token='" + access_token + '\'' + ", app_key='" + app_key + '\'' + ", sign='" + sign
				+ '\'' + ", timestamp='" + timestamp + '\'' + ", format='" + format + '\'' + ", v='" + v + '\'' + '}';
	}

	public String convert2ReqParam() {
		return convert(JSONObject.toJSONString(memberAndProtocolInfoJson));
	}

	private String convert(String reqparam) {
		JSONObject result = new JSONObject();
		result.put("method", this.getMethod());
		result.put("access_token", this.getAccess_token());
		result.put("app_key", this.getApp_key());
		result.put("sign", this.getSign());
		result.put("timestamp", this.getTimestamp());
		result.put("format", this.getFormat());
		result.put("v", this.getV());
		if (StringUtils.isNotBlank(memberAndProtocolInfoJson.asJsonPropertiesKey())) {
			result.put(memberAndProtocolInfoJson.asJsonPropertiesKey(), reqparam);
		} else {
			result.putAll(JSONObject.parseObject(reqparam, Map.class));
		}
		return result.toJSONString();
	}

	public String buildSign() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.isNotBlank(memberAndProtocolInfoJson.asJsonPropertiesKey())) {
			JSONObject json = new JSONObject();
			json.put(memberAndProtocolInfoJson.asJsonPropertiesKey(), JSONObject.toJSONString(memberAndProtocolInfoJson));
			builder.append("360buy_param_json").append(json);
		} else {
			builder.append("360buy_param_json").append(JSONObject.toJSONString(memberAndProtocolInfoJson));
		}
		if (StringUtils.isNotBlank(this.getAccess_token())) {
			builder.append("access_token").append(this.getAccess_token());
		}
		builder.append("app_key").append(this.getApp_key())
				.append("method").append(this.getMethod())
				.append("sign_method").append("md5")
				.append("timestamp").append(this.getTimestamp())
				.append("v").append(this.getV());
		return builder.toString();
	}

	public String convertUrlString() {
		Map<String, Object> result = new HashMap<>();

		if (StringUtils.isNotBlank(memberAndProtocolInfoJson.asJsonPropertiesKey())) {
			JSONObject json = new JSONObject();
			json.put(memberAndProtocolInfoJson.asJsonPropertiesKey(), JSONObject.toJSONString(memberAndProtocolInfoJson));
			result.put("360buy_param_json", json.toJSONString());
		} else {
			result.put("360buy_param_json", JSONObject.toJSONString(memberAndProtocolInfoJson));
		}
		if (StringUtils.isNotBlank(this.getAccess_token())) {
			result.put("access_token", this.getAccess_token());
		}

		result.put("app_key", this.getApp_key());
		result.put("method", this.getMethod());
		result.put("sign_method", "md5");
		result.put("timestamp", this.getTimestamp());
		result.put("v", this.getV());
		result.put("sign", this.getSign());
//		result.put("format", this.getFormat());

		return HttpUtil.toParams(result);
	}
	public Map<String, Object> convertMapParam() {
		Map<String, Object> result = new HashMap<>();
		result.put("method", this.getMethod());
		if (StringUtils.isNotBlank(this.getAccess_token())) {
			result.put("access_token", this.getAccess_token());
		}
		result.put("app_key", this.getApp_key());
		result.put("sign", this.getSign());
		result.put("sign_method", "md5");
		result.put("timestamp", this.getTimestamp());
		result.put("format", this.getFormat());
		result.put("v", this.getV());
		if (StringUtils.isNotBlank(memberAndProtocolInfoJson.asJsonPropertiesKey())) {
			JSONObject json = new JSONObject();
			json.put(memberAndProtocolInfoJson.asJsonPropertiesKey(), JSONObject.toJSONString(memberAndProtocolInfoJson));
			result.put("360buy_param_json", json.toJSONString());
		} else {
			result.put("360buy_param_json", JSONObject.toJSONString(memberAndProtocolInfoJson));
		}
		return result;
	}
}
