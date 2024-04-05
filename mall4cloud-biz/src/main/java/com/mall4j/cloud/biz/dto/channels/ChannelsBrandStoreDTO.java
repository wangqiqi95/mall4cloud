package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("视频号4.0查询品牌库DTO")
public class ChannelsBrandStoreDTO {
	
	@ApiModelProperty("每页数量(默认10, 不超过50)")
	private Integer pageSize;
	
	@ApiModelProperty("由上次请求返回, 记录翻页的上下文, 传入时会从上次返回的结果往后翻一页, 不传默认拉取第一页数据")
	private String nextKey;
}
