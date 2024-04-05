package com.mall4j.cloud.flow.vo;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-06-02
 */
public class SystemVO {
    private Integer userNums;
    private Integer systemType;
    /**
     * 全部
     */
    private Integer all;
    /**
     * pc
     */
    private Integer pc;
    /**
     * h5
     */
    private Integer h5;
    /**
     * 小程序
     */
    private Integer applets;
    /**
     * 安卓
     */
    private Integer android;
    /**
     * ios
     */
    private Integer ios;

    public Integer getUserNums() {
        return userNums;
    }

    public void setUserNums(Integer userNums) {
        this.userNums = userNums;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getPc() {
        return pc;
    }

    public void setPc(Integer pc) {
        this.pc = pc;
    }

    public Integer getH5() {
        return h5;
    }

    public void setH5(Integer h5) {
        this.h5 = h5;
    }

    public Integer getApplets() {
        return applets;
    }

    public void setApplets(Integer applets) {
        this.applets = applets;
    }

    public Integer getAndroid() {
        return android;
    }

    public void setAndroid(Integer android) {
        this.android = android;
    }

    public Integer getIos() {
        return ios;
    }

    public void setIos(Integer ios) {
        this.ios = ios;
    }

    @Override
    public String toString() {
        return "SystemVO{" +
                "userNums=" + userNums +
                ", systemType=" + systemType +
                ", all=" + all +
                ", pc=" + pc +
                ", h5=" + h5 +
                ", applets=" + applets +
                ", android=" + android +
                ", ios=" + ios +
                '}';
    }

    public SystemVO(){
        pc = 0;
        h5 = 0;
        applets = 0;
        android = 0;
        ios = 0;
    }
}
