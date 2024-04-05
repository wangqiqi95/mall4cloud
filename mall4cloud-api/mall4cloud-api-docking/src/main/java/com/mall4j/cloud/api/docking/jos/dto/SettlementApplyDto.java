package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.enums.JosBusiness;
import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @description: 益世企业综合服务发佣申请接口, 请求参数
 * @date 2021/12/26 13:15
 */
public class SettlementApplyDto implements IJosParam {

	private String josBusinessCode;

	@ApiModelProperty(value = "平台编号,益世分配，例如：“XXXX”")
	private String appCode;

	@ApiModelProperty(value = "业务规则编号,益世分配，例如：“1111”")
	private String ruleNo;

	@ApiModelProperty(value = "发佣信息产生的相关业务订单编号", required = true)
	private String businessNo;

	@ApiModelProperty(value = "请求编号，标志每次请求，业务自定 ，保证每次请求唯一", required = true)
	private String requestId;
	@ApiModelProperty(value = "证件号码，身份证的最后一位若为X，建议大写", required = true)
	private String certNo;

	/**
	 * 传代码201
	 * ("201","居民身份证"),
	 * ("202", "军官证"),
	 * ("203", "武警警官证"),
	 * ("204", "士兵证"),
	 * ("208", "外国护照"),
	 * ("209", "港澳同胞回乡证"),
	 * ("211","台胞证"),
	 * ("213","台湾居民来往大陆通行证"),
	 * ("214","大陆居民往来台湾通行证")
	 * ("210","港澳居民来往内地通行证")
	 * ("237","中华人民共和国港澳居民居住证")
	 *
	 * {@link com.mall4j.cloud.api.docking.jos.enums.CertType}
	 */
	@ApiModelProperty(value = "证件类型", required = true)
	private String certType;

	@ApiModelProperty(value = "WIRE：电汇；\n" + "JDPAY：京东网银钱包；\n" + "WXQB：微信钱包；\n" + "JDXJK：京东小金库；\n" + "PLATFORM_PAY：平台代付；\n", required = true)
	private String payType;

	@ApiModelProperty(value = "例如张三，此姓名必须和身份证上的姓名一致", required = true)
	private String cardName;

	@ApiModelProperty(value = "银行卡号，京东pin，二级商户号，微信openid等", required = true)
	private String cardNo;

	@ApiModelProperty(value = "电汇必传：传银行开户行支行名称\n" + "微信必传：传获取openid的公众号APPID\n")
	private String bankName;

	@ApiModelProperty(value = "电汇必传：支行联行号")
	private String bankUnionCode;

	@ApiModelProperty(value = "（1：企业、2：个人），不传默认个人")
	private Integer accountType;

	@ApiModelProperty(value = "银行卡在开户行绑定的手机号例如：13800001111")
	private String cardPhone;

	@ApiModelProperty(value = "0：含税金额【税前金额】，1 ：不含税金额【税后金额】 \n" + "【税前金额】表示amount包含税费，例如amount：1000，先开票扣除税金，发到手少于1000 \n"
			+ "【税后金额】表示amount不含税费，例如amount：1000，直接发到手1000\n", required = true)
	private Integer amountWay;

	@ApiModelProperty(value = "发佣金额，2位小数，不可低于一角（0.1），最高不要超过499999.99万 \n" + "【微信钱包】最低0.3元，最高300000元\n", required = true)
	private BigDecimal amount;

	@ApiModelProperty(value = "期望付款日期 到达此日期时财务出款，日期格式 (“yyyy-MM-dd HH:mm:ss”)，不传默认为财务审核后实时出款")
	private String approvedDate;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
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

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getCardPhone() {
		return cardPhone;
	}

	public void setCardPhone(String cardPhone) {
		this.cardPhone = cardPhone;
	}

	public Integer getAmountWay() {
		return amountWay;
	}

	public void setAmountWay(Integer amountWay) {
		this.amountWay = amountWay;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public SettlementApplyDto(JosBusiness josBusiness) {
		this.josBusinessCode = josBusiness.getBusinessCode();
	}

	public String getJosBusinessCode() {
		return josBusinessCode;
	}

	public void setJosBusinessCode(String josBusinessCode) {
		this.josBusinessCode = josBusinessCode;
	}

	public SettlementApplyDto() {
	}

	@Override
	public String toString() {
		return "SettlementApplyDto{" +
				"josBusinessNo='" + josBusinessCode + '\'' +
				", appCode='" + appCode + '\'' +
				", ruleNo='" + ruleNo + '\'' +
				", businessNo='" + businessNo + '\'' +
				", requestId='" + requestId + '\'' +
				", certNo='" + certNo + '\'' +
				", certType='" + certType + '\'' +
				", payType='" + payType + '\'' +
				", cardName='" + cardName + '\'' +
				", cardNo='" + cardNo + '\'' +
				", bankName='" + bankName + '\'' +
				", bankUnionCode='" + bankUnionCode + '\'' +
				", accountType=" + accountType +
				", cardPhone='" + cardPhone + '\'' +
				", amountWay=" + amountWay +
				", amount=" + amount +
				", approvedDate='" + approvedDate + '\'' +
				'}';
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
		if (JosBusiness.JointVentureCommission.getBusinessCode().equals(this.josBusinessCode)) {
			this.ruleNo = context.getJointVentureRuleNo();
		} else {
			this.ruleNo = context.getRuleNo();
		}
		if (StringUtils.isBlank(this.requestId)) {
			this.requestId = context.getRequestId();
		}
	}
}
