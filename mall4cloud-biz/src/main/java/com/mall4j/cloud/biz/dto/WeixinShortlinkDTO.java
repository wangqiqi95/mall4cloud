package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
public class WeixinShortlinkDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("小程序页面路径")
    private String pagePath;

}
