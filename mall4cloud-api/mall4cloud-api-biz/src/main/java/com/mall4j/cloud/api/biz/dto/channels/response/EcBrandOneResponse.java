package com.mall4j.cloud.api.biz.dto.channels.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcAuditResult;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrand;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
  * 视频号4.0查询品牌资质审核响应实体
  */
@Data
public class EcBrandOneResponse extends EcBaseResponse {
	
	@JsonProperty("brand")
	private EcBrand ecBrand;
	
	//申请单状态 (1审核中,2审核失败,3已生效,4已撤回,5即将过期(不影响商品售卖),6已过期
	private Integer status;
	
	//创建时间
	@JsonProperty("create_time")
	private Long createTime;
	
	//更新时间
	@JsonProperty("update_time")
	private Long updateTime;
	
	//审核结果
	@JsonProperty("audit_result")
	EcAuditResult ecAuditResult;
}
