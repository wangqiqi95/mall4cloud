package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: requestId查询会员信息审核状态 请求响应
 * @date 2021/12/25 14:50
 */
public class QueryMemberStatusResp implements Serializable {

	private static final long serialVersionUID = -7898017330408814623L;
	/**
	 * 1	成功
	 * 0	失败
	 * 0	参数错误
	 * 0	平台不存在
	 */
	@ApiModelProperty("接口返回code,1:成功，0:失败")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public QueryMemberStatusResp() {
	}

	public QueryMemberStatusResp(Integer code, String msg) {
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

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "QueryMemberStatusResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public static class Data {

		@ApiModelProperty("平台编号")
		private String appCode;

		@ApiModelProperty("会员申请Id")
		private String requestId;

		/**
		 * 1	通过
		 * 2	驳回
		 * 3	待审核
		 * 4	会员信息不存在，请检查传入参数
		 */
		@ApiModelProperty("审核是否通过，1:通过,2:驳回,3:待审核,4:会员信息不存在，请检查传入参数")
		private Integer result;

		@ApiModelProperty("审核结果描述")
		private String remark;

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

		public Integer getResult() {
			return result;
		}

		public void setResult(Integer result) {
			this.result = result;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		@Override
		public String toString() {
			return "Data{" + "appCode='" + appCode + '\'' + ", requestId='" + requestId + '\'' + ", result=" + result + ", remark='" + remark + '\'' + '}';
		}
	}
}
