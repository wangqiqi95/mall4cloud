package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 四、	益世企业综合服务发佣申请接口,响应
 * @date 2021/12/26 13:15
 */
public class SettlementApplyResp implements Serializable {

	private static final long serialVersionUID = 6127362637382751524L;
	/**
	 * 1	成功
	 * 0	失败
	 * -1	参数错误
	 * -2	平台不存在
	 * -4	申请ID重复
	 * -5	平台已关闭
	 * -7	自然人信息不存在
	 * -10	自然人信息审核未通过
	 * -11	自然人信息待审核
	 * -13	发佣金额不可低于0.1元
	 * -35	银行卡姓名和证件姓名不一致
	 * -38	发佣金额不可高于499999.99元
	 */
	@ApiModelProperty("接口返回code,1\t成功\n" + "0\t失败\n" + "-1\t参数错误\n" + "-2\t平台不存在\n" + "-4\t申请ID重复\n" + "-5\t平台已关闭\n" + "-7\t自然人信息不存在\n" + "-10\t自然人信息审核未通过\n"
			+ "-11\t自然人信息待审核\n" + "-13\t发佣金额不可低于0.1元\n" + "-35\t银行卡姓名和证件姓名不一致\n" + "-38\t发佣金额不可高于499999.99元\n")
	private Integer code;

	@ApiModelProperty("接口返回msg")
	private String msg;

	private Data data;

	public SettlementApplyResp() {
	}

	public SettlementApplyResp(Integer code, String msg) {
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

	public static class Data {

		@ApiModelProperty(value = "发佣申请ID,对应入参requestId，便于上游处理请求", required = true)
		private String requestId;

		@ApiModelProperty(value = "益世产生订单号,益世产生订单号，便于定位订单")
		private String orderNo;

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		@Override
		public String toString() {
			return "Data{" + "requestId='" + requestId + '\'' + ", orderNo='" + orderNo + '\'' + '}';
		}
	}

	@Override
	public String toString() {
		return "SettlementApplyResp{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
