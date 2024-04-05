package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 五、	企业综合服务发佣信息修改接口,请求参数
 * @date 2021/12/26 13:45
 */
public class SettlementUpdateDto implements IJosParam {

	@ApiModelProperty(value = "平台编号,益世分配，例如：“XXXX”")
	private String appCode;

	@ApiModelProperty(value = "发佣申请ID，发佣申请ID，依据此字段查询对应的发佣申请单", required = true)
	private String requestId;

	@ApiModelProperty(value = "收款账号,银行卡号，二级商户号，京东pin，微信openid", required = true)
	private String cardNo;

	@ApiModelProperty(value = "银行开户行支行名称,电汇必传：传银行开户行支行名称\n" + "微信必传：传获取openid的公众号APPID\n")
	private String bankName;

	@ApiModelProperty(value = "银行联行号,电汇必传：支行联行号")
	private String bankUnionCode;

	@ApiModelProperty(value = "收款账户性质,（1：企业、2：个人），不传默认个人")
	private String accountType;

	@ApiModelProperty(value = "手机号,银行卡在开户行绑定的手机号例如：13800001111")
	private String cardPhone;

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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankUnionCode() {
		return bankUnionCode;
	}

	public void setBankUnionCode(String bankUnionCode) {
		this.bankUnionCode = bankUnionCode;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCardPhone() {
		return cardPhone;
	}

	public void setCardPhone(String cardPhone) {
		this.cardPhone = cardPhone;
	}

	@Override
	public String toString() {
		return "SettlementUpdateDto{" + "appCode='" + appCode + '\'' + ", requestId='" + requestId + '\'' + ", cardNo='" + cardNo + '\'' + ", bankName='"
				+ bankName + '\'' + ", bankUnionCode='" + bankUnionCode + '\'' + ", accountType='" + accountType + '\'' + ", cardPhone='" + cardPhone + '\''
				+ '}';
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
