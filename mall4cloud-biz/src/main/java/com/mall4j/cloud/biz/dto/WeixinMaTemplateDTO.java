package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信小程序模板素材表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
@Data
public class WeixinMaTemplateDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("小程序名称")
    private String maAppName;

    @ApiModelProperty("小程序appid")
    private String maAppId;

    @ApiModelProperty("小程序路径")
    private String maAppPath;

    @ApiModelProperty("卡片标题")
    private String cardTitle;

    @ApiModelProperty("卡片封面")
    private String cardImg;

    @ApiModelProperty("展示方式： 1文字 2图片 3小程序卡片")
    private Integer showType;

    @ApiModelProperty("小程序头像")
    private String headImage;

    @ApiModelProperty("是否授权：0否 1是")
    private Integer authorizationState;

    @ApiModelProperty("微信素材id")
    private String thumbMediaId;
}
