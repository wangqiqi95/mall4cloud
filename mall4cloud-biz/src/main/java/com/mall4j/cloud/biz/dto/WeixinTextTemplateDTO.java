package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 微信文本模板表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
@Data
public class WeixinTextTemplateDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

//    @ApiModelProperty("模板名称")
//    private String templateName;

    @NotEmpty
    @ApiModelProperty("文字内容")
    private String templateContent;

    @ApiModelProperty("超文本链接信息")
    private String textHerfs;

//    @ApiModelProperty("公众号原始id")
//    private String appId;
}
