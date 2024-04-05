package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微信消息模板DTO
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
@Data
public class WeixinTmessageStatusDTO {
    private static final long serialVersionUID = 1L;

	@NotEmpty
    @ApiModelProperty("id")
    private String id;

	@NotNull
    @ApiModelProperty("0禁用 1启用")
    private Integer status;

}
