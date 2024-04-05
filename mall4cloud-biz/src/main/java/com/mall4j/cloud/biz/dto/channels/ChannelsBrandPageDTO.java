package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("视频号4.0查询品牌DTO")
public class ChannelsBrandPageDTO {
	
	@ApiModelProperty("品牌名称")
	private String brandName;
	
	@ApiModelProperty("审核单状态, 不填默认拉全部 (0默认值,1审核中,2审核失败,3审核通过(包括即将过期和已过期),4已撤回)")
	private Integer status;

}
