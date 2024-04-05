package com.mall4j.cloud.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 快递鸟物流详情查询
 * @author SJL
 */
public class Quick100 {

    /**
     * 快递100中顺丰编码
     */
    @JsonIgnore
    public final static String SF_CODE = "shunfeng";
    /**
     * 快递100中丰网速运编码
     */
    @JsonIgnore
    public final static String FENGWANG_CODE = "fengwang";

    private String customer;
    private String key;
    private String reqUrl;
    private Boolean isOpen;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
