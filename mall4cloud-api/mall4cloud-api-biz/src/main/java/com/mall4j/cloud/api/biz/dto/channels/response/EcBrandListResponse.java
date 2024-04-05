package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0获取品牌资质申请列表响应实体
 */
@Data
public class EcBrandListResponse extends EcBaseResponse {
	
	//品牌资质申请信息
	private List<EcBrandResponse> brands;
	
	//品牌资质总数
	@JsonProperty("total_num")
	private Integer totalNum;
	
	//本次翻页的上下文，用于请求下一页
	@JsonProperty("next_key")
	private String nextKey;

}
