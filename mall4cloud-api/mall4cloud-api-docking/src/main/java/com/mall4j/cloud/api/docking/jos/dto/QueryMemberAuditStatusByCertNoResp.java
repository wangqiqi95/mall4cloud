package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 三、	根据证件号码查询会员审核状态响应
 * @date 2021/12/25 16:21
 */
public class QueryMemberAuditStatusByCertNoResp  {

	/**
	 * 1 成功
	 * 0 失败
	 * -1 参数错误
	 * -2 平台不存在
	 * -5 平台已关闭
	 */
	@ApiModelProperty("接口返回code,1:成功，0:失败")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public QueryMemberAuditStatusByCertNoResp() {
	}

	public QueryMemberAuditStatusByCertNoResp(Integer code, String msg) {
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
		return "QueryMemberAuditStatusByCertNoResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public static class Data {
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
			return "Data{" + "result=" + result + ", remark='" + remark + '\'' + '}';
		}
	}
}
