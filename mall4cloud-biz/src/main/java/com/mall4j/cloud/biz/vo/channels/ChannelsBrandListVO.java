package com.mall4j.cloud.biz.vo.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 视频号品牌资质申请列表VO
 */
@Data
public class ChannelsBrandListVO {
	
	@ApiModelProperty("品牌")
	private List<ChannelsBrandVO> channelsBrandVOS;
	
	@ApiModelProperty("品牌资质总数")
	private Integer totalNum;
	
}
