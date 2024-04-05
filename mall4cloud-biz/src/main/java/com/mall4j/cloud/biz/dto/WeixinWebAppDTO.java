package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信公众号表DTO
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
@Data
public class WeixinWebAppDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("公众号头像")
    private String appImgUrl;

    @ApiModelProperty("应用类型")
    private String applicationType;

    @ApiModelProperty("微信二维码图片")
    private String qrcodeimg;

    @ApiModelProperty("微信号")
    private String weixinNumber;

    @ApiModelProperty("微信AppId")
    private String weixinAppId;

    @ApiModelProperty("微信AppSecret")
    private String weixinAppSecret;

    @ApiModelProperty("公众号类型")
    private String accountType;

    @ApiModelProperty("计划类型")
    private Integer playType;

    @ApiModelProperty("接入方式")
    private Integer accessMode;

    @ApiModelProperty("有效期")
    private Date validityTime;

    @ApiModelProperty("是否认证")
    private String authStatus;

    @ApiModelProperty("Access_Token")
    private String accessToken;

    @ApiModelProperty("token获取的时间")
    private Date tokenGettime;

    @ApiModelProperty("api凭证")
    private String apiticket;

    @ApiModelProperty("apiticket获取时间")
    private Date apiticketGettime;

    @ApiModelProperty("jsapiticket")
    private String jsapiticket;

    @ApiModelProperty("jsapiticket获取时间")
    private Date jsapiticketGettime;

    @ApiModelProperty("类型：1手动授权，2扫码授权")
    private String authType;

    @ApiModelProperty("功能的开通状况：1代表已开通")
    private String businessInfo;

    @ApiModelProperty("公众号授权给开发者的权限集")
    private String funcInfo;

    @ApiModelProperty("授权方昵称")
    private String nickName;

    @ApiModelProperty("授权方头像")
    private String headimgurl;

    @ApiModelProperty("授权信息")
    private String authorizationInfo;

    @ApiModelProperty("刷新token")
    private String authorizerRefreshToken;

    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("授权状态（1正常，2已取消）")
    private String authorizationStatus;

    @ApiModelProperty("创建人登录名称")
    private String createBy;

    @ApiModelProperty("修改人登录名称")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;
}
