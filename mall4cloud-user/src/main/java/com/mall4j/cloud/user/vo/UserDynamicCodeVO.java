package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 动态会员码VO
 */
@Data
public class UserDynamicCodeVO {
	
	@ApiModelProperty("动态会员码")
	private String dynamicCode;
	
	@ApiModelProperty("有效期时间(秒级)")
	private Integer expires;
}
