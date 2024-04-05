package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0品牌库请求实体
 */
@Data
public class EcBrandStoreRequest {
	
	//每页数量(默认10, 不超过50)
	@JsonProperty("page_size")
	private Integer pageSize;
	
	//由上次请求返回, 记录翻页的上下文, 传入时会从上次返回的结果往后翻一页, 不传默认拉取第一页数据
	@JsonProperty("next_key")
	private String nextKey;
}
