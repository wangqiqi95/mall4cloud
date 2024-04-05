package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class GetCodeDto {

    @ApiModelProperty(value = "爱铺货应用appkey", required = true)
    @NotBlank(message = "appkey不能为空")
    private String appkey;

    @ApiModelProperty("返回的令牌类型，固定为 “code”")
    private String responsetype;

    @ApiModelProperty(value = "商城在爱铺货的标识，与爱铺货对接开始后，会给一个标识字符串", required = true)
    @NotBlank(message = "state不能为空")
    private String state;

    @ApiModelProperty(value = "回调地址，固定值")
//    @NotBlank(message = "redirecturi不能为空")
    private String redirecturi;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getResponsetype() {
        return responsetype;
    }

    public void setResponsetype(String responsetype) {
        this.responsetype = responsetype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRedirecturi() {
        return redirecturi;
    }

    public void setRedirecturi(String redirecturi) {
        this.redirecturi = redirecturi;
    }

    @Override
    public String toString() {
        return "GetCodeDto{" + "appkey='" + appkey + '\'' + ", responsetype='" + responsetype + '\'' + ", state='" + state + '\'' + ", redirecturi='"
                + redirecturi + '\'' + '}';
    }
}
