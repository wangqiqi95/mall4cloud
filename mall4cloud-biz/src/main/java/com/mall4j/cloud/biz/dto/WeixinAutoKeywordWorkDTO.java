package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微信关键字表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
@Data
public class WeixinAutoKeywordWorkDTO {
    private static final long serialVersionUID = 1L;

	@NotEmpty
    @ApiModelProperty("")
    private String id;

	@NotNull
    @ApiModelProperty("是否启用： 0否 1是")
    private Integer isWork;

}
