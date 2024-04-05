package com.mall4j.cloud.common.bean;

/**
 * 阿里物流详情查询
 * @author SJL
 */
public class AliQuick {

    /**
     * 阿里物流中的顺丰编号
     */
    public final static String SF_CODE = "SFEXPRESS";

    private String aliCode;
    private Boolean isOpen;
    private String reqUrl;

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getAliCode() {
        return aliCode;
    }

    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }
}
