package com.mall4j.cloud.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 导购标签DTO
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class StaffTagDTO{

	@ApiModelProperty("标签组ID")
	private Long tagGroupId;

	@ApiModelProperty(value = "标签名")
	private String tagName;

}
