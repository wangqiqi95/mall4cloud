package com.mall4j.cloud.api.biz.dto.channels.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcBasicBrand;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0品牌库响应实体
 */
@Data
public class EcBrandStoreResponse extends EcBaseResponse {
	
	//品牌库中的品牌信息
	private List<EcBasicBrand> brands;

	//本次翻页的上下文，用于请求下一页
	@JsonProperty("next_key")
	private String nextKey;
	
	//是否还有下一页内容
	@JsonProperty("continue_flag")
	private Boolean	continueFlag;
	
}
