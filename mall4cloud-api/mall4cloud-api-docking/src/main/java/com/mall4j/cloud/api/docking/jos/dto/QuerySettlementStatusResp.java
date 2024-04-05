package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 六、	企业综合服务发佣状态查询接口,请求响应
 * @date 2021/12/26 13:58
 */
public class QuerySettlementStatusResp implements Serializable {

	private static final long serialVersionUID = -821871561454187231L;
	/**
	 * 1	成功
	 * 0	失败
	 * -1	参数错误
	 * -25	发佣申请不存在
	 */
	@ApiModelProperty("接口返回code,1\t成功\n" + "0\t失败\n" + "-1\t参数错误\n" + "-25\t发佣申请不存在\n")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public QuerySettlementStatusResp() {
	}

	public QuerySettlementStatusResp(Integer code, String msg) {
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
		return "QuerySettlementStatusResp{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public static class Data {
		@ApiModelProperty(value = "发佣申请ID,对应入参requestId，便于上游处理请求")
		private String requestId;

		@ApiModelProperty(value = "状态,发佣状态：1发佣中、2发佣成功、3发佣失败、13退票 请务必关注此项，收到发佣失败状态3，退票状态13，都是因为收款账户信息不正确导致，请一定要调用（3.2企业综合服务发佣信息修改接口）将正确的银行信息传入继续发佣，如某个人存在发佣失败或者退票的订单，不允许此人新的申请接入，原因：发佣失败、退票某些情况下的益世已经开票，如果一直处于失败状态，益世需要频繁作废发票，影响结算平台发票核销流程、出款时效；跨月退票的订单因上月已经统计计入应付兆盈金额，即使退票也会收取。\n"
				+ "状态路径可能情况\n" + "第一种：1->3\n" + "第二种：1->2\n" + "第三种：1->2->13（成功之后，收款银行当天或者隔天或者几天后有可能会因为某种原因退回，简称退票）\n")
		private Integer status;

		@ApiModelProperty(value = "备注,发佣失败原因，发佣失败返回")
		private String remark;

		@ApiModelProperty(value = "实发佣金,发佣成功后返回，实际打款给自然人的金额")
		private String actualAmount;

		@ApiModelProperty(value = "需支付给兆盈金额,发佣成功后返回，发佣申请接口发佣金额*约定比例")
		private String payAmount;

		@ApiModelProperty(value = "税费,产生的税费（以税后金额发佣，此税费仅供参考，以月末账单汇总为准）")
		private String taxFee;

		@ApiModelProperty(value = "含税金额,发佣成功后返回，实发佣金+税费")
		private String taxPreAmount;

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
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

		public String getTaxFee() {
			return taxFee;
		}

		public void setTaxFee(String taxFee) {
			this.taxFee = taxFee;
		}

		public String getTaxPreAmount() {
			return taxPreAmount;
		}

		public void setTaxPreAmount(String taxPreAmount) {
			this.taxPreAmount = taxPreAmount;
		}

		@Override
		public String toString() {
			return "Data{" + "requestId='" + requestId + '\'' + ", status=" + status + ", remark='" + remark + '\'' + ", actualAmount='" + actualAmount + '\''
					+ ", payAmount='" + payAmount + '\'' + ", taxFee='" + taxFee + '\'' + ", taxPreAmount='" + taxPreAmount + '\'' + '}';
		}
	}
}
