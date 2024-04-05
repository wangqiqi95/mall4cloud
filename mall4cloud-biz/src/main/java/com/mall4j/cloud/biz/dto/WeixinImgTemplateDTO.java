package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 微信图片模板表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
@Data
public class WeixinImgTemplateDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @NotEmpty
    @ApiModelProperty("模板图片")
    private String templateImg;

    @ApiModelProperty("微信素材id")
    private String mediaId;

//    @ApiModelProperty("公众号原始id")
//    private String appId;

}
