package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0撤回品牌资质审核
 */
@Data
public class EcBrandCancelRequest {
	
	//品牌库中的品牌编号
	@JsonProperty("brand_id")
	private String brandId;
	
	//要撤销的审核单 ID , 提交审核成功后返回
	@JsonProperty("audit_id")
	private String auditId;
	
}
