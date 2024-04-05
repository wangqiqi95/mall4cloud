package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcCompanyInfo;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0物流响应实体
 */
@Data
public class EcDeliveryResponse extends EcBaseResponse {
	
	//快递公司列表
	@JsonProperty("company_list")
	private List<EcCompanyInfo> companyList;
}
