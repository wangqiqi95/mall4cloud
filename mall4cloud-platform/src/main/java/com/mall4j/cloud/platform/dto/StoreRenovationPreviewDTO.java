package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺装修信息DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
public class StoreRenovationPreviewDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("装修内容")
    private String content;
}
