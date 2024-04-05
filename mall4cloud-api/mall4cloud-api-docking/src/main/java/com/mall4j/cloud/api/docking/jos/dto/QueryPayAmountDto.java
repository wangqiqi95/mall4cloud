package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 七、	企业综合服务查询应付金额接口,请求参数
 * @date 2021/12/26 14:10
 */
public class QueryPayAmountDto implements IJosParam {
	@ApiModelProperty(value = "平台编号,益世分配，例如：“XXXX”")
	private String appCode;

	@ApiModelProperty(value = "发佣申请ID，发佣申请ID，依据此字段查询对应的应付金额", required = true)
	private String requestId;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "QueryPayAmountDto{" + "appCode='" + appCode + '\'' + ", requestId='" + requestId + '\'' + '}';
	}

	/**
	 * 当请求参数为
	 *
	 * @return
	 */
	@Override
	public String asJsonPropertiesKey() {
		return "request";
	}

	@Override
	public void setJosContext(JosIntefaceContext context) {
		this.appCode = context.getPlatformCode();
	}
}
