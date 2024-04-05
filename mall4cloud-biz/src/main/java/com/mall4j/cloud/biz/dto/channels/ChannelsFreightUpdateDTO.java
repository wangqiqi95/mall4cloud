package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChannelsFreightUpdateDTO {
	@ApiModelProperty("运费模板id")
	@NotNull(message = "运费模板id不能为空")
	private Long transportId;
	
	@ApiModelProperty("微信侧运费模板id")
	@NotNull(message = "微信侧运费模板id不能为空")
	private String wxTemplateId;
}
