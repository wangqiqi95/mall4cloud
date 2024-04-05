package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EcAuditResult {
	
	//提审的审核单ID
	@JsonProperty("audit_id")
	private Long auditId;
	
	//审核不通过的原因, 审核成功不返回
	@JsonProperty("reject_reason")
	private String rejectReason;
}
