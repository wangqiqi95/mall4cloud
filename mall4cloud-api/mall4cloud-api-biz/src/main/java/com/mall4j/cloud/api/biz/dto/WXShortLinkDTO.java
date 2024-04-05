package com.mall4j.cloud.api.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短链DTO
 */
@Data
public class WXShortLinkDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("小程序页面路径")
    private String pagePath;

}
