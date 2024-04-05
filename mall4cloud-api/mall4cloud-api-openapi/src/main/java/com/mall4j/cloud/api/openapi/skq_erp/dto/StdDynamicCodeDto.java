package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * POS端动态会员码DTO
 */
@Data
public class StdDynamicCodeDto implements Serializable, IStdDataCheck{
	private static final long serialVersionUID = 5807275938174405860L;
	
	@ApiModelProperty(value = "POS端动态会员码")
	private String dynamicCode;
	
	@Override
	public StdResult check() {
		if (StringUtils.isBlank(dynamicCode)) {
			return StdResult.fail("dynamicCode不能为空");
		}
		return StdResult.success();
	}
}
