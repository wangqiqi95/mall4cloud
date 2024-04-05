package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信素材链接表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
@Data
public class WeixinLinksucaiDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("外部链接")
    private String outerLink;

//    @ApiModelProperty("原始公众号id")
//    private String appId;

}
