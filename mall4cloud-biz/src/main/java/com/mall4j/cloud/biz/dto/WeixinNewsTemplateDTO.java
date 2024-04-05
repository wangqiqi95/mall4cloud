package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 微信图文模板表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
@Data
public class WeixinNewsTemplateDTO{
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @ApiModelProperty("模板id")
    private String id;

//    @ApiModelProperty("模板名称")
//    private String templateName;
//
//    @ApiModelProperty("模板类型：1单图文 2多图文")
//    private String templateType;
//
//    @ApiModelProperty("模板来源： 0微信图文素材 1自动回复素材")
//    private Integer fromType;
//
//    @ApiModelProperty("图文素材媒体id")
//    private String mediaId;
}
