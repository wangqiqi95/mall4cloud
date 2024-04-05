package com.mall4j.cloud.common.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 快递鸟物流详情查询
 * @author FrozenWatermelon
 */
public class QuickBird {

    /**
     * 快递鸟中顺丰编码
     */
    @JsonIgnore
    public final static String SF_CODE = "SF";

    private String eBusinessID;
    private String appKey;
    private String reqUrl;
    private Boolean isOpen;

    public String geteBusinessID() {
        return eBusinessID;
    }

    public void seteBusinessID(String eBusinessID) {
        this.eBusinessID = eBusinessID;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }
}
