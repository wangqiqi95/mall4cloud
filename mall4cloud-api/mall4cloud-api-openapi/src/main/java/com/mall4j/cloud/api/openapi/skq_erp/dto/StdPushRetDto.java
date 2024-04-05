package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 类描述：中台回传推订单/退单结果回执数据
 *
 * @date 2022/1/9 9:49：00
 */
public class StdPushRetDto implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = 614836037531315867L;
	private String requestId;

	@ApiModelProperty(value = "平台订单号")
	private String tid;

	@ApiModelProperty(value = "是否成功")
	private Boolean isSuccess;

	@ApiModelProperty(value = "类型（order:推订单回执、refund：推退单回执）")
	private String type;

	@ApiModelProperty(value = "错误信息")
	private String errorMsg;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "StdPushRetDto{" + "requestId='" + requestId + '\'' + ", tid='" + tid + '\'' + ", isSuccess=" + isSuccess + ", type='" + type + '\''
				+ ", errorMsg='" + errorMsg + '\'' + '}';
	}

	@Override
	public StdResult check() {
		if (StringUtils.isBlank(requestId)) {
			return StdResult.fail("requestId不能为空");
		}
		if (StringUtils.isBlank(tid)) {
			return StdResult.fail("tid不能为空");
		}
		if (isSuccess == null) {
			return StdResult.fail("isSuccess不能为空");
		}
		if (StringUtils.isBlank(type)) {
			return StdResult.fail("type不能为空");
		}
		return StdResult.success();
	}
}
