package com.mall4j.cloud.flow.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * @Author lth
 * @Date 2021/6/3 10:03
 */
public class FlowUserAnalysisVO {

    @ApiModelProperty("访问深度")
    private List<VisitNum> visitPageList;

    @ApiModelProperty("访客地域分布")
    private List<UserAnalysisVO> visitUserList;

    @ApiModelProperty("系统设备-pc")
    private Integer pc;

    @ApiModelProperty("系统设备-h5")
    private Integer h5;

    @ApiModelProperty("系统设备-小程序")
    private Integer applets;

    @ApiModelProperty("系统设备-安卓")
    private Integer android;

    @ApiModelProperty("系统设备-ios")
    private Integer ios;

    public static class VisitNum {
        /**
         * 访问页面数量 (编号:访问页面数量)1:1 2:2 3:3 4:4 5:5 6:6-10 7:11-20 8:20+
         */
        private Integer visitId;

        /**
         * 用户数
         */
        private Integer userNums;

        public Integer getVisitId() {
            return visitId;
        }

        public void setVisitId(Integer visitId) {
            this.visitId = visitId;
        }

        public Integer getUserNums() {
            return userNums;
        }

        public void setUserNums(Integer userNums) {
            this.userNums = userNums;
        }

        @Override
        public String toString() {
            return "VisitNum{" +
                    "visitId=" + visitId +
                    ", userNums=" + userNums +
                    '}';
        }

        public VisitNum(Integer visitId, Integer userNums) {
            this.visitId = visitId;
            this.userNums = userNums;
        }
    }

    public List<VisitNum> getVisitPageList() {
        return visitPageList;
    }

    public void setVisitPageList(List<VisitNum> visitPageList) {
        this.visitPageList = visitPageList;
    }

    public List<UserAnalysisVO> getVisitUserList() {
        return visitUserList;
    }

    public void setVisitUserList(List<UserAnalysisVO> visitUserList) {
        this.visitUserList = visitUserList;
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
        return "FlowUserAnalysisVO{" +
                "visitPageList=" + visitPageList +
                ", visitUserList=" + visitUserList +
                ", pc=" + pc +
                ", h5=" + h5 +
                ", applets=" + applets +
                ", android=" + android +
                ", ios=" + ios +
                '}';
    }
}
