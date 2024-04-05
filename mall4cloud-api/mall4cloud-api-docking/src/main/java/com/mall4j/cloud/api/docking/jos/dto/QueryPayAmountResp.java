package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 七、	企业综合服务查询应付金额接口,请求响应
 * @date 2021/12/26 14:11
 */
public class QueryPayAmountResp implements Serializable {

	private static final long serialVersionUID = -8821657579029126927L;
	/**
	 * 1	成功
	 * 0	失败
	 * -1	参数错误
	 * -25	发佣申请不存在
	 * -26	不支持该类订单查询
	 */
	@ApiModelProperty("接口返回code,1\t成功\n" + "0\t失败\n" + "-1\t参数错误\n" + "-25\t发佣申请不存在\n" + "-26\t不支持该类订单查询\n")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public QueryPayAmountResp() {
	}

	public QueryPayAmountResp(Integer code, String msg) {
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
		return "QueryPayAmountResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public static class Data {
		@ApiModelProperty(value = "发佣申请ID,对应入参requestId，便于上游处理请求", required = true)
		private String requestId;

		@ApiModelProperty(value = "申请金额，对应发佣申请接口入参amount字段", required = true)
		private String applyAmount;

		@ApiModelProperty(value = "应发佣金，打款给自然人的金额", required = true)
		private String actualAmount;

		@ApiModelProperty(value = "需支付给兆盈金额，企业应付金额", required = true)
		private String payAmount;

		@ApiModelProperty(value = "总费率，例如：0.087000", required = true)
		private String totalRate;

		@ApiModelProperty(value = "自然人承担费率,例如：0.000001", required = true)
		private String personalRate;

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getApplyAmount() {
			return applyAmount;
		}

		public void setApplyAmount(String applyAmount) {
			this.applyAmount = applyAmount;
		}

		public String getActualAmount() {
			return actualAmount;
		}

		public void setActualAmount(String actualAmount) {
			this.actualAmount = actualAmount;
		}

		public String getPayAmount() {
			return payAmount;
		}

		public void setPayAmount(String payAmount) {
			this.payAmount = payAmount;
		}

		public String getTotalRate() {
			return totalRate;
		}

		public void setTotalRate(String totalRate) {
			this.totalRate = totalRate;
		}

		public String getPersonalRate() {
			return personalRate;
		}

		public void setPersonalRate(String personalRate) {
			this.personalRate = personalRate;
		}

		@Override
		public String toString() {
			return "Data{" + "requestId='" + requestId + '\'' + ", applyAmount='" + applyAmount + '\'' + ", actualAmount='" + actualAmount + '\''
					+ ", payAmount='" + payAmount + '\'' + ", totalRate='" + totalRate + '\'' + ", personalRate='" + personalRate + '\'' + '}';
		}
	}
}
