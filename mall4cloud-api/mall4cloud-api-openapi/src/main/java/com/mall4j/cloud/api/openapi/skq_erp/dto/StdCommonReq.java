package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @description: 中台调用公共参数
 * @date 2022/1/6 22:07
 */
public class StdCommonReq implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = -6868068678839180139L;
	@ApiModelProperty(value = "方法类型")
	private String method;

	@ApiModelProperty(value = "加签")
	private String sign;

	@ApiModelProperty(value = "版本（默认为v1）")
	private String version;

	@ApiModelProperty(value = "请求时的时间戳")
	private Long timestamp;

	@ApiModelProperty(value = "请求数据")
	private String postData;

	public String getPostData() {
		return postData;
	}

	public void setPostData(String postData) {
		this.postData = postData;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "StdCommonReq{" + "method='" + method + '\'' + ", sign='" + sign + '\'' + ", version='" + version + '\'' + ", timestamp=" + timestamp + '}';
	}

	@Override
	public StdResult check() {
		if (StringUtils.isBlank(method)) {
			return StdResult.fail("method不能为空");
		}
		if (StringUtils.isBlank(sign)) {
			return StdResult.fail("sign不能为空");
		}
		if (StringUtils.isBlank(version)) {
			return StdResult.fail("version不能为空");
		}

		if (timestamp == null) {
			return StdResult.fail("timestamp不能为空");
		}
		return StdResult.success();
	}
}
