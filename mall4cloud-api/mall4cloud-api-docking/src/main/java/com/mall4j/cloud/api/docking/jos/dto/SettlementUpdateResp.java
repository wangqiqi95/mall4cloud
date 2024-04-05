package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 五、	企业综合服务发佣信息修改接口,响应
 * @date 2021/12/26 13:45
 */
public class SettlementUpdateResp implements Serializable {

	private static final long serialVersionUID = 8181835274597957653L;
	/**
	 * 1	成功
	 * 0	失败
	 * -1	参数错误
	 * -2	平台不存在
	 * -5	平台已关闭
	 * -25	发佣申请ID不存在
	 * -26	发佣状态不是失败，不可修改
	 */
	@ApiModelProperty("接口返回code,1\t成功\n" + "0\t失败\n" + "-1\t参数错误\n" + "-2\t平台不存在\n" + "-5\t平台已关闭\n" + "-25\t发佣申请ID不存在\n" + "-26\t发佣状态不是失败，不可修改\n")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public SettlementUpdateResp() {
	}

	public SettlementUpdateResp(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SettlementUpdateResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data {
		@ApiModelProperty(value = "发佣申请ID,对应入参requestId，便于上游处理请求", required = true)
		private String requestId;

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		@Override
		public String toString() {
			return "Data{" + "requestId='" + requestId + '\'' + '}';
		}
	}
}
