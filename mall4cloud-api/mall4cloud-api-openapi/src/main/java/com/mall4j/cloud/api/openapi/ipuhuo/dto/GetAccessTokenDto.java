package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class GetAccessTokenDto {

    @ApiModelProperty(value = "爱铺货的应用ID", required = true)
    @NotBlank(message = "appkey不能为空")
    private String appkey;

    @ApiModelProperty(value = "爱铺货的应用Secret", required = true)
    @NotBlank(message = "appsecret不能为空")
    private String appsecret;

    @ApiModelProperty(value = "授与方式（固定为 “authorization_code”）", required = true)
    @NotBlank(message = "granttype不能为空")
    private String granttype;

    @ApiModelProperty(value = "商家授权后返回的授权码（Authorization Code）", required = true)
    @NotBlank(message = "code不能为空")
    private String code;

    @ApiModelProperty(value = "授权回调地址（该值必须与获取 Authorization Code 时传递的 redirecturi 保持一致）", required = true)
    @NotBlank(message = "redirecturi不能为空")
    private String redirecturi;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getGranttype() {
        return granttype;
    }

    public void setGranttype(String granttype) {
        this.granttype = granttype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirecturi() {
        return redirecturi;
    }

    public void setRedirecturi(String redirecturi) {
        this.redirecturi = redirecturi;
    }

    @Override
    public String toString() {
        return "GetAccessTokenDto{" + "appkey='" + appkey + '\'' + ", appsecret='" + appsecret + '\'' + ", granttype='" + granttype + '\'' + ", code='" + code
                + '\'' + ", redirecturi='" + redirecturi + '\'' + '}';
    }
}
