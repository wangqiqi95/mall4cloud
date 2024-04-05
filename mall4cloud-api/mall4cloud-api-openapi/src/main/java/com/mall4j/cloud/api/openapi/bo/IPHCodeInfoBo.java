package com.mall4j.cloud.api.openapi.bo;

import com.mall4j.cloud.api.openapi.constant.SysTypeEnum;

public class IPHCodeInfoBo implements ITokenInfoBo {

    private String state;
    private String redirecturi;
    private String appkey;

    public IPHCodeInfoBo() {
    }

    public IPHCodeInfoBo(String state, String redirecturi, String appkey) {
        this.state = state;
        this.redirecturi = redirecturi;
        this.appkey = appkey;
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

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String toString() {
        return "IPHCodeInfoBo{" + "state='" + state + '\'' + ", redirecturi='" + redirecturi + '\'' + ", appkey='" + appkey + '\'' + '}';
    }

    @Override
    public SysTypeEnum getSysType() {
        return SysTypeEnum.IPH;
    }
}
