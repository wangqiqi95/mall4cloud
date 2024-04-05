package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 微信关注回复DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
@Data
public class WeixinSubscribeOpenDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开关： 1:开/2:关")
    private String openState;

    @NotEmpty
    @ApiModelProperty("公众号id")
    private String appId;
}
