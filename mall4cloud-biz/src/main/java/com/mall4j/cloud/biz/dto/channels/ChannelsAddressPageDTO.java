package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ChannelsAddressPageDTO {
	@ApiModelProperty("收货人名称")
	private String receiverName;
	
	@ApiModelProperty("收货人手机号")
	private String telNumber;
}
