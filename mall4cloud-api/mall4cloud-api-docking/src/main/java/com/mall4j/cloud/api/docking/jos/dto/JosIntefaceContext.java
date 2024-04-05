package com.mall4j.cloud.api.docking.jos.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class JosIntefaceContext {

	/**
	 * 平台编号,益世分配，例如：“XXXX”
	 */
	private String platformCode;

	/**
	 * 业务规则编号,益世分配，例如：“1111”
	 */
	private String ruleNo;

	/**
	 * 业务规则编号-联营分佣
	 */
	private String jointVentureRuleNo;

	private String requestId;

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getJointVentureRuleNo() {
		return jointVentureRuleNo;
	}

	public void setJointVentureRuleNo(String jointVentureRuleNo) {
		this.jointVentureRuleNo = jointVentureRuleNo;
	}

	public String getRequestId() {
		if (StringUtils.isBlank(this.requestId)) {
			this.requestId = UUID.randomUUID().toString().replace("-", "");
		}
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "JosIntefaceContext{" +
				"platformCode='" + platformCode + '\'' +
				", ruleNo='" + ruleNo + '\'' +
				", jointVentureRuleNo='" + jointVentureRuleNo + '\'' +
				", requestId='" + requestId + '\'' +
				'}';
	}
}
