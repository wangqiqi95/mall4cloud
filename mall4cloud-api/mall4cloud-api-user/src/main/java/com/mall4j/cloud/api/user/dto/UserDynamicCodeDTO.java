package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDynamicCodeDTO {
	
	@ApiModelProperty(value = "POS端动态会员码")
	private String dynamicCode;
}
