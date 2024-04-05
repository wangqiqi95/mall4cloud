package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微信菜单表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuPutDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private String id;

    @ApiModelProperty("父id")
    private String fatherId;

//    @NotEmpty
//    @ApiModelProperty("菜单类型: 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型")
//    private String menuType;

    @NotEmpty
    @ApiModelProperty("菜单名称")
    private String menuName;

//    @NotEmpty
//    @ApiModelProperty("相应消息类型")
//    private String msgtype;

//    @NotNull
//    @ApiModelProperty("菜单位置")
//    private Integer orders;

    @NotEmpty
    @ApiModelProperty("公众号原始id")
    private String appId;

}
