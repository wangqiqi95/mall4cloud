package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信菜单表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private String id;

    @ApiModelProperty("父id")
    private String fatherId;

    @ApiModelProperty("菜单KEY")
    private String menuKey;

    @ApiModelProperty("菜单类型")
    private String menuType;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("网页链接")
    private String url;

    @ApiModelProperty("相应消息类型")
    private String msgtype;

    @ApiModelProperty("菜单位置")
    private Integer orders;

    @ApiModelProperty("关联素材id")
    private String templateId;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("小程序appid")
    private String miniprogramAppid;

    @ApiModelProperty("小程序页面路径")
    private String miniprogramPagepath;

}
