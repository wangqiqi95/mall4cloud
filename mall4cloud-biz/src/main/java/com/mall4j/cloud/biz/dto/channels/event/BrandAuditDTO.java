package com.mall4j.cloud.biz.dto.channels.event;

import lombok.Data;

@Data
public class BrandAuditDTO {
	
	private String brandId;
	private String auditId;
	private Integer status;
	private String reason;
}
