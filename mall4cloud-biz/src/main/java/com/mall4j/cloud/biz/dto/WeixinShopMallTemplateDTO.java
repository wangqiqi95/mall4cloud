package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信商城功能配置DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
@Data
public class WeixinShopMallTemplateDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标题")
    private String cardTitle;

    @ApiModelProperty("小程序appid")
    private String maAppId;

    @ApiModelProperty("小程序路径")
    private String maAppPath;

}
