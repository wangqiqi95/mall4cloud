package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class RefreshTokenDto {

    @ApiModelProperty(value = "授与方式（固定为 “refreshtoken”）", required = true)
    private String granttype;

    @ApiModelProperty(value = "用于刷新令牌 Token 的 Refreshtoken", required = true)
    @NotBlank(message = "refreshtoken不能为空")
    private String refreshtoken;

    @ApiModelProperty(value = "爱铺货的应用ID", required = true)
    @NotBlank(message = "appkey不能为空")
    private String appkey;

    @ApiModelProperty(value = "爱铺货的应用Secret", required = true)
    @NotBlank(message = "appsecret不能为空")
    private String appsecret;

    public String getGranttype() {
        return granttype;
    }

    public void setGranttype(String granttype) {
        this.granttype = granttype;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

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

    @Override
    public String toString() {
        return "RefreshTokenDto{" + "granttype='" + granttype + '\'' + ", refreshtoken='" + refreshtoken + '\'' + ", appkey='" + appkey + '\'' + ", appsecret='"
                + appsecret + '\'' + '}';
    }
}
