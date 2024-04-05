package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0获取品牌资质申请列表响应实体
 */
@Data
public class EcBrandListRequest {
	
	//每页数量(默认10, 不超过50)
	@JsonProperty("page_size")
	private Integer pageSize;
	
	//审核单状态, 不填默认拉全部 (0默认值,1审核中,2审核失败,3审核通过(包括即将过期和已过期),4已撤回)
	private Integer status;
	
	//由上次请求返回, 记录翻页的上下文, 传入时会从上次返回的结果往后翻一页, 不传默认拉取第一页数据
	@JsonProperty("next_key")
	private String nextKey;
}
