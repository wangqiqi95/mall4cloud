package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 三、	根据证件号码查询会员审核状态 请求参数
 * @date 2021/12/25 16:21
 */
public class QueryMemberAuditStatusByCertNoDto implements IJosParam {
	private static final long serialVersionUID = -3494844569466135650L;
	@ApiModelProperty(value = "平台编号")
	private String platformCode;

	@ApiModelProperty(value = "证件号码,身份证的最后一位若为X，建议大写", required = true)
	private String certNo;

	/**
	 * {@link com.mall4j.cloud.api.docking.jos.enums.CertType}
	 */
	@ApiModelProperty(value = "证件类型,(\"201\",\"居民身份证\"),\n" + "(\"202\", \"军官证\"),\n" + "(\"203\", \"武警警官证\"),\n" + "(\"204\", \"士兵证\"),\n"
			+ "(\"208\", \"外国护照\"),\n" + "(\"209\", \"港澳同胞回乡证\"),\n" + "(\"211\",\"台胞证\"),\n" + "(\"213\",\"台湾居民来往大陆通行证\"),\n" + "(\"214\",\"大陆居民往来台湾通行证\")\n"
			+ "(\"210\",\"港澳居民来往内地通行证\")\n" + "(\"237\",\"中华人民共和国港澳居民居住证\")\n" + "(\"238\",\"中华人民共和国台湾居民居住证\")\n", required = true)
	private String certType;

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
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

	@Override
	public String toString() {
		return "QueryMemberAuditStatusByCertNoDto{" + "platformCode='" + platformCode + '\'' + ", certNo='" + certNo + '\'' + ", certType='" + certType + '\''
				+ '}';
	}

	/**
	 * 当请求参数为
	 *
	 * @return
	 */
	@Override
	public String asJsonPropertiesKey() {
		return "requestJson";
	}

	@Override
	public void setJosContext(JosIntefaceContext context) {
		this.platformCode = context.getPlatformCode();
	}
}
