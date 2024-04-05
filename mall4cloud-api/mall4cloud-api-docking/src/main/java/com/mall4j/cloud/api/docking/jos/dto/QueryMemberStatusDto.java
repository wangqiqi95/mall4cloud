package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 二、	requestId查询会员信息审核状态 请求参数
 * @date 2021/12/25 14:47
 */
public class QueryMemberStatusDto implements IJosParam {
	private static final long serialVersionUID = 5919026029851988797L;
	@ApiModelProperty(value = "平台编号")
	private String platformCode;

	@ApiModelProperty(value = "请求编号，标志每次请求，业务自定 ，保证每次请求唯一", required = true)
	private String requestId;

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "QueryMemberStatusDto{" + "platformCode='" + platformCode + '\'' + ", requestId='" + requestId + '\'' + '}';
	}

	@Override
	public void setJosContext(JosIntefaceContext context) {
		this.platformCode = context.getPlatformCode();
		if (StringUtils.isBlank(this.requestId)) {
			this.requestId = context.getRequestId();
		}
	}
}
